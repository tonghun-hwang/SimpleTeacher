package com.example.simpleteacher.main;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

//https://dpdpwl.tistory.com/37

public class FragmentLastGraph extends Fragment {

    private LineChart chart;
    View inf;

    public FragmentLastGraph() {
        // Required empty public constructor
    }

    private ItemActivity activity;
    private Data mData;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //이 메소드가 호출될떄는 프래그먼트가 엑티비티위에 올라와있는거니깐 getActivity메소드로 엑티비티참조가능
        activity = (ItemActivity) getActivity();
        mData = (Data) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inf = inflater.inflate(R.layout.fragment_lastgraph, container, false);
        generateGraphFromDB(null);

        return inf;
    }

    public void generateGraphFromDB(String studID) {

        if (studID == null) {
            return;
        }
        // // Chart Style // //
        chart = inf.findViewById(R.id.chart);

        // disable description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // set listeners
        chart.setDrawGridBackground(false);


        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // force pinch zoom along both axis
        chart.setPinchZoom(true);

        List<Entry> entries = new ArrayList<>();

        if (activity.mAnalysisTrainingDB == null || !activity.mAnalysisTrainingDB.isOpen()) {
            activity.mAnalysisTrainingDB = activity.mAnalysisTrainingDBHelper.getWritableDatabase();
        }

        String tableName = "training_s1";
        String column;
        float data;
        int tableMax = 4;
        int loopMax = 5;

        for (int j = 0; j < tableMax; j++) {
            tableName = "training_s" + (j + 1);
            Cursor c = activity.mAnalysisTrainingDB.rawQuery("SELECT * FROM " + tableName
                            + " WHERE USERID = '" + studID + "'"
                    , null);

            int cursorCount = c.getCount();
            if (cursorCount == 3 && j < 3) {
                c.moveToFirst();
                for (int i = 1; i <= loopMax; i++) {
                    int index = i + j * 5;
                    column = "WORD_T" + i;
                    data = c.getFloat(c.getColumnIndex(column));
                    entries.add(new Entry(index, data));
                }
            } else if (cursorCount == 3 && j == 3) {
                c.moveToFirst();
                int i = 1;
                int index = 16;
                column = "WORD_T" + i;
                data = c.getFloat(c.getColumnIndex(column));
                entries.add(new Entry(index, data));
            } else {
                for (int i = 1; i <= loopMax; i++) {
                    int index = j * 5 + i;
                    entries.add(new Entry(index, 0));
                }
            }
            c.close();
        }
        // TODO: setup data of session 16
        entries.add(new Entry(16, 0));

        // X Axis
        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(16f, 16f, 0f);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(16);
        xAxis.setValueFormatter(xAxisFormatter);


        // Y left axis
        YAxis yLAxis = chart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setAxisMaximum(150f);
        yLAxis.setAxisMinimum(0f);
        yLAxis.enableGridDashedLine(10f, 10f, 0f);

        // Y right axis
        YAxis yRAxis = chart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        LineDataSet lineDataSet = new LineDataSet(entries, "Prozentualer Fehleranteil pro Trainingseinheit");
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(true);

        LineData lineData = new LineData(lineDataSet);
        chart.setData(lineData);

        Description description = new Description();
        description.setText("");

        chart.setDoubleTapToZoomEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDescription(description);
        chart.invalidate();
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
