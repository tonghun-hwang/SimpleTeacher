package com.example.simpleteacher.main;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.example.simpleteacher.Data;
import com.example.simpleteacher.ItemActivity;
import com.example.simpleteacher.R;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class FragmentOneFive extends Fragment {
    private String TAG = "Main.Frag";
    View inf;
    private TextView errorCategory, numAllWords, numProzWrongAll, numProzWrongError, numNochmal, numARadierer, numALLRadierer;
    private LineChart grafikA, grafikB, grafikC;

    public FragmentOneFive() {
        // Required empty public constructor
    }

    private ItemActivity mParent;
    private Data mData;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //이 메소드가 호출될떄는 프래그먼트가 엑티비티위에 올라와있는거니깐 getActivity메소드로 엑티비티참조가능
        mParent = (ItemActivity) context;
        mData = (Data) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inf = inflater.inflate(R.layout.fragment_onefive, container, false);

        filledDataFromDB(0, null);
        generateGraphFromDB(0, null);

        return inf;
    }

    public void initValues () {
        numAllWords.setText("0");
        numProzWrongAll.setText("0.00%");
        numProzWrongError.setText("0.00%");
        numNochmal.setText("0");
        numARadierer.setText("0");
        numALLRadierer.setText("0");
    }

    public void filledDataFromDB(int sesBlock, String studID) {
        String tableName = "training_s" + (sesBlock + 1);

        errorCategory = (TextView) inf.findViewById(R.id.errorcategory);
        numAllWords = (TextView) inf.findViewById(R.id.numAllWords);
        numProzWrongAll = (TextView) inf.findViewById(R.id.prozWrongAll);
        numProzWrongError = (TextView) inf.findViewById(R.id.prozWrongError);
        numNochmal = (TextView) inf.findViewById(R.id.numNochmal);
        numARadierer = (TextView) inf.findViewById(R.id.numARadierer);
        numALLRadierer = (TextView) inf.findViewById(R.id.numAllesRadierer);

        if (studID == null) {
            initValues();
            return;
        }

        if (sesBlock >= 0 && sesBlock < 3) {
            errorCategory.setText(mParent.sesCategory);
        } else {
            errorCategory.setText("--");
        }

        if (mParent.mAnalysisTrainingDB == null || !mParent.mAnalysisTrainingDB.isOpen()) {
            mParent.mAnalysisTrainingDB = mParent.mAnalysisTrainingDBHelper.getWritableDatabase();
        }

        Cursor c = mParent.mAnalysisTrainingDB.rawQuery("SELECT * FROM " + tableName
                        + " WHERE USERID = '" + studID + "'"
                , null);
        if (c.getCount() == 3) {
            c.moveToPosition(0);
            String numAllWordsString = c.getString(c.getColumnIndex("WORD_TOTAL"))
                    + " (" + c.getString(c.getColumnIndex("WORD_CAT_TOTAL"))
                    + ")";
            numAllWords.setText(numAllWordsString);

            double percent = c.getDouble(c.getColumnIndex("WORD_ER")) * 100;
            String txtPercent = String.format("%.1f%%", percent);
            numProzWrongAll.setText(txtPercent);

            percent = c.getDouble(c.getColumnIndex("WORD_CAT_ER")) * 100;
            txtPercent = String.format("%.1f%%", percent);
            numProzWrongError.setText(txtPercent);

            numNochmal.setText(
                    c.getString(c.getColumnIndex("NUM_EAR")));
            numARadierer.setText(
                    c.getString(c.getColumnIndex("NUM_ONE_ERASE")));
            numALLRadierer.setText(
                    c.getString(c.getColumnIndex("NUM_A_ERASE")));
        } else {
            initValues();
            return;
        }

        c.close();
        if (mParent.mAnalysisTrainingDB != null) {
            if (mParent.mAnalysisTrainingDB.isOpen()) {
                mParent.mAnalysisTrainingDB.close();
            }
        }
    }

    public void filledData(Data data) {
        mData = data;
        int sesBlock = mData.mSessionBlock;

        if (sesBlock >= 0 && sesBlock < 3) {
            errorCategory = (TextView) inf.findViewById(R.id.errorcategory);
            errorCategory.setText(mData.errorcategory[sesBlock]);
        }
        // category 2)
        numAllWords = (TextView) inf.findViewById(R.id.numAllWords);
        numAllWords.setText(Integer.toString(mData.numAllWords[sesBlock]));

        numProzWrongAll = (TextView) inf.findViewById(R.id.prozWrongAll);
        numProzWrongAll.setText(Double.toString(mData.prozAllTrainWrong[sesBlock]));

        numProzWrongError = (TextView) inf.findViewById(R.id.prozWrongError);
        numProzWrongError.setText(Double.toString(mData.prozAllTrainWrong[sesBlock]));

        numNochmal = (TextView) inf.findViewById(R.id.numNochmal);
        numNochmal.setText(Integer.toString(mData.arrNochmal[sesBlock]));

        numARadierer = (TextView) inf.findViewById(R.id.numARadierer);
        numARadierer.setText(Integer.toString(mData.arrAErase[sesBlock]));

        numALLRadierer = (TextView) inf.findViewById(R.id.numAllesRadierer);
        numALLRadierer.setText(Integer.toString(mData.arrAllErase[sesBlock]));
    }

    public void generateGraphFromDB(int sesBlock, String studID) {
        if (studID == null) {
            return;
        }
        // category 3)
        // grafik a
        grafikA = inf.findViewById(R.id.chart1);

        // background color
        //   chart.setBackgroundColor(Color.WHITE);

        // disable description text
        grafikA.getDescription().setEnabled(false);

        // enable touch gestures
        grafikA.setTouchEnabled(true);

        // set listeners
        grafikA.setDrawGridBackground(false);


        // enable scaling and dragging
        grafikA.setDragEnabled(true);
        grafikA.setScaleEnabled(true);

        // force pinch zoom along both axis
        grafikA.setPinchZoom(true);

        List<Entry> entriesA = new ArrayList<>();

        int loopMax = 5;
        if (sesBlock == 3) {
            loopMax = 1;
        }
        Log.d(TAG, "sesBlock: " + sesBlock);
        if (mParent.mAnalysisTrainingDB == null || !mParent.mAnalysisTrainingDB.isOpen()) {
            mParent.mAnalysisTrainingDB = mParent.mAnalysisTrainingDBHelper.getWritableDatabase();
        }

        String tableName = "training_s" + (sesBlock + 1);
        Cursor c = mParent.mAnalysisTrainingDB.rawQuery("SELECT * FROM " + tableName
                        + " WHERE USERID = '" + studID + "'"
                , null);
        String column;
        float data;
        int cursorCount = c.getCount();
        if (cursorCount == 3) {
           c.moveToFirst();
            for (int i = 0; i < loopMax; i++) {
                int index = i + 1;

                column = "WORD_T" + index;
                data = c.getFloat(c.getColumnIndex(column));
                entriesA.add(new Entry(index, data));
            }
        } else {
            for (int i = 0; i < loopMax; i++) {
                int index = sesBlock * 5 + i + 1;
                entriesA.add(new Entry(index, 0));
            }
        }

        // X Axis
        ValueFormatter xAxisFormatterA = new FragmentOneFive.DayAxisValueFormatter(grafikA);
        XAxis xAxisA = grafikA.getXAxis();
        xAxisA.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisA.setTextColor(Color.BLACK);
        xAxisA.enableGridDashedLine(5f, 5f, 0f);
        xAxisA.setGranularity(1f); // only intervals of 1 day
        xAxisA.setLabelCount(5);
        xAxisA.setValueFormatter(xAxisFormatterA);


        // Y left axis
        YAxis yLAxisA = grafikA.getAxisLeft();
        yLAxisA.setTextColor(Color.BLACK);
        yLAxisA.setAxisMaximum(150f);
        yLAxisA.setAxisMinimum(0f);
        yLAxisA.enableGridDashedLine(10f, 10f, 2f);

        // Y right axis
        YAxis yRAxisA = grafikA.getAxisRight();
        yRAxisA.setDrawLabels(false);
        yRAxisA.setDrawAxisLine(false);
        yRAxisA.setDrawGridLines(false);

        LineDataSet lineDataSetA = new LineDataSet(entriesA, "Anzahl geschriebener Wörter");
        lineDataSetA.setLineWidth(2f);
        lineDataSetA.setCircleRadius(3f);
        lineDataSetA.setCircleColor(Color.parseColor("#FFFF0000"));
        lineDataSetA.setColor(Color.parseColor("#FFFF0000"));
        lineDataSetA.setDrawCircleHole(true);
        lineDataSetA.setDrawCircles(true);
        lineDataSetA.setDrawHorizontalHighlightIndicator(false);
        lineDataSetA.setDrawHighlightIndicators(false);
        lineDataSetA.setDrawValues(true);

        LineData lineDataA = new LineData(lineDataSetA);
        grafikA.setData(lineDataA);

        Description descriptionA = new Description();
        descriptionA.setText("");

        grafikA.setDoubleTapToZoomEnabled(false);
        grafikA.setDrawGridBackground(false);
        grafikA.setDescription(descriptionA);
        grafikA.invalidate();

        //grafikB

        grafikB = inf.findViewById(R.id.chart2);

        // background color
        //   chart.setBackgroundColor(Color.WHITE);

        // disable description text
        grafikB.getDescription().setEnabled(false);

        // enable touch gestures
        grafikB.setTouchEnabled(true);

        // set listeners
        grafikB.setDrawGridBackground(false);

        // enable scaling and dragging
        grafikB.setDragEnabled(true);
        grafikB.setScaleEnabled(true);
        // force pinch zoom along both axis
        grafikB.setPinchZoom(true);

        List<Entry> entriesB = new ArrayList<>();
        List<Entry> entriesB2 = new ArrayList<>();
        List<Entry> entriesB3 = new ArrayList<>();

        if (cursorCount == 3) {
            c.moveToFirst();
            for (int i = 0; i < loopMax; i++) {
                int index = i + 1;
                column = "WORD_T" + index + "_ERROR";
                data = c.getFloat(c.getColumnIndex(column));
                entriesB.add(new Entry(index, data));
            }

            c.moveToNext();
            for (int i = 0; i < loopMax; i++) {
                int index = i + 1;
                column = "WORD_T" + index + "_ERROR";
                data = c.getFloat(c.getColumnIndex(column));
                entriesB2.add(new Entry(index, data));
            }

            c.moveToNext();
            for (int i = 0; i < loopMax; i++) {
                int index = i + 1;
                column = "WORD_T" + index + "_ERROR";
                data = c.getFloat(c.getColumnIndex(column));
                entriesB3.add(new Entry(index, data));
            }
        } else {
            for (int i = 0; i < loopMax; i++) {
                int index = sesBlock * 5 + i + 1;
                entriesB.add(new Entry(index, 0));
            }
        }

        // X Axis
        ValueFormatter xAxisFormatterB = new FragmentOneFive.DayAxisValueFormatter(grafikB);
        XAxis xAxisB = grafikB.getXAxis();
        xAxisB.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisB.setTextColor(Color.BLACK);
        xAxisB.enableGridDashedLine(5f, 5f, 0f);
        xAxisB.setGranularity(1f); // only intervals of 1 day
        xAxisB.setLabelCount(5);
        xAxisB.setValueFormatter(xAxisFormatterB);

        // Y left axis
        YAxis yLAxisB = grafikB.getAxisLeft();
        yLAxisB.setTextColor(Color.BLACK);
        yLAxisB.setAxisMaximum(100f);
        yLAxisB.setAxisMinimum(0f);
        yLAxisB.enableGridDashedLine(10f, 10f, 0f);

        // Y right axis
        YAxis yRAxisB = grafikB.getAxisRight();
        yRAxisB.setDrawLabels(false);
        yRAxisB.setDrawAxisLine(false);
        yRAxisB.setDrawGridLines(false);

        LineDataSet lineDataSetB = new LineDataSet(entriesB, "First");
        lineDataSetB.setLineWidth(2f);
        lineDataSetB.setCircleRadius(3f);
        lineDataSetB.setCircleColor(Color.parseColor("#FFA41D13"));
        lineDataSetB.setColor(Color.parseColor("#FFA41D13"));
        lineDataSetB.setDrawCircleHole(true);
        lineDataSetB.setDrawCircles(true);
        lineDataSetB.setDrawHorizontalHighlightIndicator(false);
        lineDataSetB.setDrawHighlightIndicators(false);
        lineDataSetB.setDrawValues(true);

        LineDataSet lineDataSetB2 = new LineDataSet(entriesB2, "Second");
        lineDataSetB2.setLineWidth(2f);
        lineDataSetB2.setCircleRadius(3f);
        lineDataSetB2.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSetB2.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSetB2.setDrawCircleHole(true);
        lineDataSetB2.setDrawCircles(true);
        lineDataSetB2.setDrawHorizontalHighlightIndicator(false);
        lineDataSetB2.setDrawHighlightIndicators(false);
        lineDataSetB2.setDrawValues(true);

        LineDataSet lineDataSetB3 = new LineDataSet(entriesB3, "Third");
        lineDataSetB3.setLineWidth(2f);
        lineDataSetB3.setCircleRadius(3f);
        lineDataSetB3.setCircleColor(Color.parseColor("#FFA3C072"));
        lineDataSetB3.setColor(Color.parseColor("#FFA3C072"));
        lineDataSetB3.setDrawCircleHole(true);
        lineDataSetB3.setDrawCircles(true);
        lineDataSetB3.setDrawHorizontalHighlightIndicator(false);
        lineDataSetB3.setDrawHighlightIndicators(false);
        lineDataSetB3.setDrawValues(true);

        LineData lineData = new LineData(lineDataSetB);
        lineData.addDataSet(lineDataSetB2);
        lineData.addDataSet(lineDataSetB3);
        grafikB.setData(lineData);

        Description descriptionB = new Description();
        descriptionB.setText("");

        grafikB.setDoubleTapToZoomEnabled(false);
        grafikB.setDrawGridBackground(false);
        grafikB.setDescription(descriptionB);
        grafikB.invalidate();

        //grafikC

        grafikC = inf.findViewById(R.id.chart3);

        // background color
        //   chart.setBackgroundColor(Color.WHITE);

        // disable description text
        grafikC.getDescription().setEnabled(false);

        // enable touch gestures
        grafikC.setTouchEnabled(true);

        // set listeners
        grafikC.setDrawGridBackground(false);

        // enable scaling and dragging
        grafikC.setDragEnabled(true);
        grafikC.setScaleEnabled(true);

        // force pinch zoom along both axis
        grafikC.setPinchZoom(true);

        List<Entry> entriesC = new ArrayList<>();
        List<Entry> entriesC2 = new ArrayList<>();
        List<Entry> entriesC3 = new ArrayList<>();

        if (cursorCount == 3) {
            c.moveToFirst();
            for (int i = 0; i < loopMax; i++) {
                int index = i + 1;
                column = "WORD_T" + index + "_ERCAT";
                data = c.getFloat(c.getColumnIndex(column));
                entriesC.add(new Entry(index, data));
            }

            c.moveToNext();
            for (int i = 0; i < loopMax; i++) {
                int index = i + 1;
                column = "WORD_T" + index + "_ERCAT";
                data = c.getFloat(c.getColumnIndex(column));
                entriesC2.add(new Entry(index, data));
            }

            c.moveToNext();
            for (int i = 0; i < loopMax; i++) {
                int index = i + 1;
                column = "WORD_T" + index + "_ERCAT";
                data = c.getFloat(c.getColumnIndex(column));
                entriesC3.add(new Entry(index, data));
            }
        } else {
            for (int i = 0; i < loopMax; i++) {
                int index = sesBlock * 5 + i + 1;
                entriesC.add(new Entry(index, 0));
            }
        }

        // X Axis
        ValueFormatter xAxisFormatterC = new FragmentOneFive.DayAxisValueFormatter(grafikC);
        XAxis xAxisC = grafikC.getXAxis();
        xAxisC.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisC.setTextColor(Color.BLACK);
        xAxisC.enableGridDashedLine(5f, 5f, 0f);
        xAxisC.setGranularity(1f); // only intervals of 1 day
        xAxisC.setLabelCount(5);
        xAxisC.setValueFormatter(xAxisFormatterC);

        // Y left axis
        YAxis yLAxisC = grafikC.getAxisLeft();
        yLAxisC.setTextColor(Color.BLACK);
        yLAxisC.setAxisMaximum(100f);
        yLAxisC.setAxisMinimum(0f);
        yLAxisC.enableGridDashedLine(10f, 10f, 0f);

        // Y right axis
        YAxis yRAxisC = grafikC.getAxisRight();
        yRAxisC.setDrawLabels(false);
        yRAxisC.setDrawAxisLine(false);
        yRAxisC.setDrawGridLines(false);

        LineDataSet lineDataSetC = new LineDataSet(entriesC, "First");
        lineDataSetC.setLineWidth(2f);
        lineDataSetC.setCircleRadius(3f);
        lineDataSetC.setCircleColor(Color.parseColor("#FFA41D13"));
        lineDataSetC.setColor(Color.parseColor("#FFA41D13"));
        lineDataSetC.setDrawCircleHole(true);
        lineDataSetC.setDrawCircles(true);
        lineDataSetC.setDrawHorizontalHighlightIndicator(false);
        lineDataSetC.setDrawHighlightIndicators(false);
        lineDataSetC.setDrawValues(true);

        LineDataSet lineDataSetC2 = new LineDataSet(entriesC2, "Second");
        lineDataSetC2.setLineWidth(2f);
        lineDataSetC2.setCircleRadius(3f);
        lineDataSetC2.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSetC2.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSetC2.setDrawCircleHole(true);
        lineDataSetC2.setDrawCircles(true);
        lineDataSetC2.setDrawHorizontalHighlightIndicator(false);
        lineDataSetC2.setDrawHighlightIndicators(false);
        lineDataSetC2.setDrawValues(true);

        LineDataSet lineDataSetC3 = new LineDataSet(entriesC3, "Third");
        lineDataSetC3.setLineWidth(2f);
        lineDataSetC3.setCircleRadius(3f);
        lineDataSetC3.setCircleColor(Color.parseColor("#FFA3C072"));
        lineDataSetC3.setColor(Color.parseColor("#FFA3C072"));
        lineDataSetC3.setDrawCircleHole(true);
        lineDataSetC3.setDrawCircles(true);
        lineDataSetC3.setDrawHorizontalHighlightIndicator(false);
        lineDataSetC3.setDrawHighlightIndicators(false);
        lineDataSetC3.setDrawValues(true);

        LineData lineDataC = new LineData(lineDataSetC);
        lineDataC.addDataSet(lineDataSetC2);
        lineDataC.addDataSet(lineDataSetC3);

        grafikC.setData(lineDataC);

        Description descriptionC = new Description();
        descriptionC.setText("");

        grafikC.setDoubleTapToZoomEnabled(false);
        grafikC.setDrawGridBackground(false);
        grafikC.setDescription(descriptionC);
        grafikC.invalidate();
    }

    public class DayAxisValueFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;
        public DayAxisValueFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }
        @Override
        public String getFormattedValue(float value) {
            return "T" + (int) value;
        }
    }
}
