package com.example.simpleteacher.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.example.simpleteacher.Data;
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

public class FragmentElevenFifteen extends Fragment {
    View inf;
    private TextView numAllWords, numProzWrongAll, numProzWrongError, numNochmal, numARadierer, numALLRadierer;
    private LineChart grafikA, grafikB, grafikC;

    public FragmentElevenFifteen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inf = inflater.inflate(R.layout.fragment_elevenfifteen, container, false);
        numAllWords = (TextView) inf.findViewById(R.id.numAllWords);
        numAllWords.setText(Integer.toString(Data.numAllWords11));

        numProzWrongAll = (TextView) inf.findViewById(R.id.prozWrongAll);
        numProzWrongAll.setText(Double.toString(Data.numProzAllWrong11));

        numProzWrongError = (TextView) inf.findViewById(R.id.prozWrongError);
        numProzWrongError.setText(Double.toString(Data.numProzWrongError11));

        numNochmal = (TextView) inf.findViewById(R.id.numNochmal);
        numNochmal.setText(Integer.toString(Data.numNochmal11));

        numARadierer = (TextView) inf.findViewById(R.id.numARadierer);
        numARadierer.setText(Integer.toString(Data.numARadierer11));

        numALLRadierer = (TextView) inf.findViewById(R.id.numAllesRadierer);
        numALLRadierer.setText(Integer.toString(Data.numAllRadierer11));

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

        // create marker to display box when values are selected
        //MyMarkerView mvA = new MyMarkerView(getContext(), R.layout.custom_marker_view);

        // Set the marker to the chart
        //mvA.setChartView(grafikA);
        //grafikA.setMarker(mvA);
        // Set the marker to the chart
        //mvA.setChartView(grafikA);
        //grafikA.setMarker(mvA);

        // enable scaling and dragging
        grafikA.setDragEnabled(true);
        grafikA.setScaleEnabled(true);
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        grafikA.setPinchZoom(true);

        List<Entry> entriesA = new ArrayList<>();
        entriesA.add(new Entry(1, (float) Data.trainAll11));
        entriesA.add(new Entry(2, (float) Data.trainAll12));
        entriesA.add(new Entry(3, (float) Data.trainAll13));
        entriesA.add(new Entry(4, (float) Data.trainAll14));
        entriesA.add(new Entry(5, (float) Data.trainAll15));

        // X Axis
        ValueFormatter xAxisFormatterA = new FragmentElevenFifteen.DayAxisValueFormatter(grafikA);
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
        yLAxisA.setAxisMaximum(100f);
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

        // create marker to display box when values are selected
        //MyMarkerView mvB = new MyMarkerView(getContext(), R.layout.custom_marker_view);

        // Set the marker to the chart
        //mvB.setChartView(grafikB);
        //grafikB.setMarker(mvB);
        // Set the marker to the chart
        //mvB.setChartView(grafikB);
        //grafikB.setMarker(mvB);

        // enable scaling and dragging
        grafikB.setDragEnabled(true);
        grafikB.setScaleEnabled(true);
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        grafikB.setPinchZoom(true);

        List<Entry> entriesB = new ArrayList<>();
        entriesB.add(new Entry(1, (float) Data.trainWrong11));
        entriesB.add(new Entry(2, (float) Data.trainWrong12));
        entriesB.add(new Entry(3, (float) Data.trainWrong13));
        entriesB.add(new Entry(4, (float) Data.trainWrong14));
        entriesB.add(new Entry(5, (float) Data.trainWrong15));

        // X Axis
        ValueFormatter xAxisFormatterB = new FragmentElevenFifteen.DayAxisValueFormatter(grafikB);
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

        LineDataSet lineDataSetB = new LineDataSet(entriesB, "Prozentualer Fehleranteil insgesamt*");
        lineDataSetB.setLineWidth(2f);
        lineDataSetB.setCircleRadius(3f);
        lineDataSetB.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSetB.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSetB.setDrawCircleHole(true);
        lineDataSetB.setDrawCircles(true);
        lineDataSetB.setDrawHorizontalHighlightIndicator(false);
        lineDataSetB.setDrawHighlightIndicators(false);
        lineDataSetB.setDrawValues(true);

        LineData lineData = new LineData(lineDataSetB);
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

        // create marker to display box when values are selected
        //MyMarkerView mvC = new MyMarkerView(getContext(), R.layout.custom_marker_view);

        // Set the marker to the chart
        //mvC.setChartView(grafikC);
        //grafikC.setMarker(mvC);
        // Set the marker to the chart
        //mvC.setChartView(grafikC);
        //grafikC.setMarker(mvC);

        // enable scaling and dragging
        grafikC.setDragEnabled(true);
        grafikC.setScaleEnabled(true);
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        grafikC.setPinchZoom(true);

        List<Entry> entriesC = new ArrayList<>();
        entriesC.add(new Entry(1, (float) Data.trainWrongError11));
        entriesC.add(new Entry(2, (float) Data.trainWrongError12));
        entriesC.add(new Entry(3, (float) Data.trainWrongError13));
        entriesC.add(new Entry(4, (float) Data.trainWrongError14));
        entriesC.add(new Entry(5, (float) Data.trainWrongError15));

        // X Axis
        ValueFormatter xAxisFormatterC = new FragmentElevenFifteen.DayAxisValueFormatter(grafikC);
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

        LineDataSet lineDataSetC = new LineDataSet(entriesC, "Prozentualer Fehleranteil Zielkategorie*");
        lineDataSetC.setLineWidth(2f);
        lineDataSetC.setCircleRadius(3f);
        lineDataSetC.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSetC.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSetC.setDrawCircleHole(true);
        lineDataSetC.setDrawCircles(true);
        lineDataSetC.setDrawHorizontalHighlightIndicator(false);
        lineDataSetC.setDrawHighlightIndicators(false);
        lineDataSetC.setDrawValues(true);

        LineData lineDataC = new LineData(lineDataSetC);
        grafikC.setData(lineDataC);


        Description descriptionC = new Description();
        descriptionC.setText("");

        grafikC.setDoubleTapToZoomEnabled(false);
        grafikC.setDrawGridBackground(false);
        grafikC.setDescription(descriptionC);
        grafikC.invalidate();

        return inf;

    }


    public class DayAxisValueFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;
        public DayAxisValueFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }
        @Override
        public String getFormattedValue(float value) {
            return "T" + (int)(value + 10);
        }
    }
}
