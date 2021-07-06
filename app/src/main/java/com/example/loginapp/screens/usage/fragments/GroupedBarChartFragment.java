package com.example.loginapp.screens.usage.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class GroupedBarChartFragment extends Fragment implements OnChartValueSelectedListener {

    private BarChart chart;
    private Typeface tfLight;
    protected final String[] months = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_barchart, container, false);
        tfLight = Typeface.createFromAsset(requireActivity().getAssets(), "HappyMonkey-Regular.ttf");
        setHasOptionsMenu(true);

        ImageView chartPopUp=v.findViewById(R.id.chartMenuPopUp);
        chartPopUp.setOnClickListener(listener->showMenu(chartPopUp));

        TextView title = v.findViewById(R.id.chartTitle);
        TextView desc = v.findViewById(R.id.chartDesc);

        title.setText(getString(R.string.groupBarChart));
        desc.setText(getString(R.string.groupBarChartDesc));

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
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTypeface(tfLight);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTypeface(tfLight);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(12);
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
        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        int groupCount = 12;
        //int startYear = 1980;
        //int endYear = startYear + groupCount;

        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();
        ArrayList<BarEntry> values3 = new ArrayList<>();
        ArrayList<BarEntry> values4 = new ArrayList<>();

        float randomMultiplier = 1500;

        for (int i = 0; i < months.length; i++) {
            values1.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
            values2.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
            values3.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
            values4.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
        }

        BarDataSet set1, set2, set3, set4;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) chart.getData().getDataSetByIndex(2);
            set4 = (BarDataSet) chart.getData().getDataSetByIndex(3);
            set1.setValues(values1);
            set2.setValues(values2);
            set3.setValues(values3);
            set4.setValues(values4);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(values1, "2019");
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(values2, "2020");
            set2.setColor(Color.rgb(164, 228, 251));
            set3 = new BarDataSet(values3, "2021");
            set3.setColor(Color.rgb(242, 247, 158));
            set4 = new BarDataSet(values4, "Projected 2022");
            set4.setColor(Color.rgb(255, 102, 0));

            BarData data = new BarData(set1, set2, set3, set4);
            data.setValueFormatter(new LargeValueFormatter());
            data.setValueTypeface(tfLight);
            TypedValue outValue = new TypedValue();
            getActivity().getTheme().resolveAttribute(R.attr.themeName, outValue, true);
            if ("dark".equals(outValue.string)) {
                set1.setValueTextColor(Color.WHITE);
                set2.setValueTextColor(Color.WHITE);
                set3.setValueTextColor(Color.WHITE);
                set4.setValueTextColor(Color.WHITE);
            }
            chart.setData(data);
        }

        // specify the width each bar should have
        chart.getBarData().setBarWidth(barWidth);

        //animateToRange(4);
        chart.setVisibleXRange(0, 4);
        chart.animateY(1500);
        chart.getXAxis().setAxisMinimum(0);
        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(0, groupSpace, barSpace);
        chart.invalidate();
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
            case R.id.actionToggleHighlight: {
                if (chart.getData() != null) {
                    chart.getData().setHighlightEnabled(!chart.getData().isHighlightEnabled());
                    chart.invalidate();
                }
                break;
            }
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