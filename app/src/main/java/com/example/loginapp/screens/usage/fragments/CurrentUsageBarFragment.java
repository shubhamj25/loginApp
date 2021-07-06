package com.example.loginapp.screens.usage.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.example.loginapp.R;
import com.example.loginapp.screens.usage.MyMarkerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class CurrentUsageBarFragment extends Fragment implements OnChartValueSelectedListener {
    private BarChart chart;
    private Typeface tfLight;
    int currentMonthNumber;
    protected final String[] months = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_barchart, container, false);
        tfLight = Typeface.createFromAsset(requireActivity().getAssets(), "HappyMonkey-Regular.ttf");
        setHasOptionsMenu(true);
        currentMonthNumber = Calendar.getInstance().get(Calendar.MONTH);
        ImageView chartPopUp = v.findViewById(R.id.chartMenuPopUp);
        chartPopUp.setOnClickListener(listener -> showMenu(chartPopUp));

        TextView title = v.findViewById(R.id.chartTitle);
        TextView desc = v.findViewById(R.id.chartDesc);

        title.setText(getString(R.string.currentUsage));
        desc.setText(getString(R.string.currentBarChartDesc));

        chart = v.findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);
        chart.setExtraOffsets(0f, 10f, 0f, 10f);
        chart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(true);
        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(requireContext(), R.layout.custom_marker_view);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        Legend l = chart.getLegend();
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setTypeface(tfLight);
        l.setYOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(12f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTypeface(tfLight);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(currentMonthNumber);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setDrawGridLines(true);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        chart.getAxisRight().setEnabled(false);

        initChart();

        TypedValue outValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.themeName, outValue, true);
        if ("dark".equals(outValue.string)) {
            leftAxis.setTextColor(Color.WHITE);
            xAxis.setTextColor(Color.WHITE);
            l.setTextColor(Color.WHITE);
            chart.setGridBackgroundColor(Color.parseColor("#50888888"));
        }
        return v;
    }


    public void initChart() {
        ArrayList<BarEntry> values = new ArrayList<>();
        float randomMultiplier = 1000;
        for (int i = 0; i < currentMonthNumber; i++) {
            values.add(new BarEntry(i + 0.5f, (float) (Math.random() * randomMultiplier)));
        }

        BarDataSet set;
        int[] barColors = new int[currentMonthNumber];
        for (int i = 0; i < barColors.length; i++) {
            barColors[i] = Color.rgb(240, 220 - 10 * i, 0);
        }
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                set = new BarDataSet(values, "Current Usage of " + LocalDate.now().getYear());
            } else {
                set = new BarDataSet(values, "Current Usage");
            }
            set.setColors(barColors);
            BarData data = new BarData(set);
            data.setValueTypeface(tfLight);
            TypedValue outValue = new TypedValue();
            getActivity().getTheme().resolveAttribute(R.attr.themeName, outValue, true);
            if ("dark".equals(outValue.string)) {
                set.setValueTextColor(Color.WHITE);
            }
            chart.setData(data);
            chart.setVisibleXRange(0, currentMonthNumber);
            chart.animateY(1500);

            for (IBarDataSet s : chart.getData().getDataSets())
                ((BarDataSet) s).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);
            chart.invalidate();
        }
    }


    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.setOnMenuItemClickListener(this::onOptionsItemSelected);
        popup.inflate(R.menu.bar);
        popup.show();
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                for (IBarDataSet set : chart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                chart.invalidate();
                break;
            }
            case R.id.actionTogglePinch: {
                chart.setPinchZoom(!chart.isPinchZoomEnabled());

                chart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                chart.setAutoScaleMinMaxEnabled(!chart.isAutoScaleMinMaxEnabled());
                chart.notifyDataSetChanged();
                break;
            }
            case R.id.actionToggleBarBorders: {
                for (IBarDataSet set : chart.getData().getDataSets())
                    ((BarDataSet) set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);

                chart.invalidate();
                break;
            }
//            case R.id.actionToggleHighlight: {
//                if (chart.getData() != null) {
//                    chart.getData().setHighlightEnabled(!chart.getData().isHighlightEnabled());
//                    chart.invalidate();
//                }
//                break;
//            }
            case R.id.animateX: {
                chart.animateX(2000);
                break;
            }
            case R.id.animateY: {
                chart.animateY(2000);
                break;
            }
            case R.id.animateXY: {
                chart.animateXY(2000, 2000);
                break;
            }
        }
        return true;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");
    }
}