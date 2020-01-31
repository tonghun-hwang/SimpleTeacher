package com.example.simpleteacher.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

//https://dpdpwl.tistory.com/37

public class FragmentLastGraph extends Fragment {

    private LineChart chart;
    View inf;

    public FragmentLastGraph() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inf = inflater.inflate(R.layout.fragment_lastgraph, container, false);

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
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        chart.setPinchZoom(true);

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, (float) Data.prozTrainWrong1));
        entries.add(new Entry(2, (float) Data.prozTrainWrong2));
        entries.add(new Entry(3, (float) Data.prozTrainWrong3));
        entries.add(new Entry(4, (float )Data.prozTrainWrong4));
        entries.add(new Entry(5, (float) Data.prozTrainWrong5));
        entries.add(new Entry(6, (float) Data.prozTrainWrong6));
        entries.add(new Entry(7, (float) Data.prozTrainWrong7));
        entries.add(new Entry(8, (float) Data.prozTrainWrong8));
        entries.add(new Entry(9, (float) Data.prozTrainWrong9));
        entries.add(new Entry(10, (float) Data.prozTrainWrong10));
        entries.add(new Entry(11, (float) Data.prozTrainWrong11));
        entries.add(new Entry(12, (float) Data.prozTrainWrong12));
        entries.add(new Entry(13, (float) Data.prozTrainWrong13));
        entries.add(new Entry(14, (float) Data.prozTrainWrong14));
        entries.add(new Entry(15, (float) Data.prozTrainWrong15));
        entries.add(new Entry(16, (float) Data.prozTrainWrong16));
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
        yLAxis.setAxisMaximum(100f);
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
        return  inf;
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
