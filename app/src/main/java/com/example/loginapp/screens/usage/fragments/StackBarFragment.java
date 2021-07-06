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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class StackBarFragment extends Fragment implements OnChartValueSelectedListener {

    private BarChart chart;
    private Typeface tfRegular;
    protected final String[] months = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    @SuppressLint("Range")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_barchart, container, false);
        setHasOptionsMenu(true);
        tfRegular = Typeface.createFromAsset(getActivity().getAssets(), "HappyMonkey-Regular.ttf");
        TextView chartTitle = v.findViewById(R.id.chartTitle);
        TextView chartDesc = v.findViewById(R.id.chartDesc);

        ImageView chartPopUp=v.findViewById(R.id.chartMenuPopUp);
        chartPopUp.setOnClickListener(listener->showMenu(chartPopUp));

        chartTitle.setText(R.string.StackedBarChart);
        chartDesc.setText(R.string.BarChartDesc);

        chart = v.findViewById(R.id.chart1);
        chart.setExtraOffsets(0f, 10f, 0f, 10f);

        chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);
        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(true);
        chart.setDrawBarShadow(false);

        chart.setDrawValueAboveBar(false);
        chart.setHighlightFullBarEnabled(false);

        // change the position of the y-labels
        YAxis leftAxis = chart.getAxisLeft();
        //leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f);// this replaces setStartAtZero(true)
        leftAxis.setTypeface(tfRegular);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        chart.getAxisRight().setEnabled(false);


        XAxis xLabels = chart.getXAxis();
        xLabels.setPosition(XAxisPosition.TOP);
        xLabels.setCenterAxisLabels(true);
        xLabels.enableGridDashedLine(10f, 10f, 0f);
        xLabels.setGranularity(1f);
        xLabels.setTypeface(tfRegular);
        xLabels.setAxisMinimum(0f);
        xLabels.setValueFormatter(new IndexAxisValueFormatter(months));
        // chart.setDrawXLabels(false);
        // chart.setDrawYLabels(false);

        // setting data
        initChart();

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setTypeface(tfRegular);
        l.setDrawInside(false);


        TypedValue outValue = new TypedValue();
        requireActivity().getTheme().resolveAttribute(R.attr.themeName, outValue, true);
        if ("dark".contentEquals(outValue.string)) {
            leftAxis.setTextColor(Color.WHITE);
            xLabels.setTextColor(Color.WHITE);
            l.setTextColor(Color.WHITE);
            chart.setGridBackgroundColor(Color.parseColor("#50888888"));
        }
        // chart.setDrawLegend(false);

        return v;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void initChart() {

        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < months.length; i++) {
            float mul = 1000;
            float val1 = (float) (Math.floor(Math.random() * (mul - 100 + 1) + 100));
            float val2 = (float) (Math.floor(Math.random() * (mul - 100 + 1) + 100));
            float val3 = (float) (Math.floor(Math.random() * (mul - 100 + 1) + 100));

            values.add(new BarEntry(
                    i+0.5f,
                    new float[]{val1, val2, val3},
                    getResources().getDrawable(R.drawable.star)));
        }

        BarDataSet set1;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Usage Comparision 2021");
            set1.setDrawIcons(false);
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"2019", "2020", "2021"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextColor(Color.WHITE);

            chart.setData(data);
        }
        for (IBarDataSet set : chart.getData().getDataSets())
            ((BarDataSet) set).setBarBorderWidth(1.f);
        set1.setValueTypeface(tfRegular);
        chart.setVisibleXRange(0,5);
        chart.animateY(1500);
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
                List<IBarDataSet> sets = chart.getData()
                        .getDataSets();

                for (IBarDataSet iSet : sets) {

                    BarDataSet set = (BarDataSet) iSet;
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }

                chart.invalidate();
                break;
            }
            case R.id.actionToggleIcons: {
                List<IBarDataSet> sets = chart.getData()
                        .getDataSets();

                for (IBarDataSet iSet : sets) {

                    BarDataSet set = (BarDataSet) iSet;
                    set.setDrawIcons(!set.isDrawIconsEnabled());
                }

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

        BarEntry entry = (BarEntry) e;

        if (entry.getYVals() != null)
            Log.i("VAL SELECTED", "Value: " + entry.getYVals()[h.getStackIndex()]);
        else
            Log.i("VAL SELECTED", "Value: " + entry.getY());
    }

    @Override
    public void onNothingSelected() {
    }

    private int[] getColors() {

        // have as many colors as stack-values per entry
        int[] colors = new int[3];

        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3);

        return colors;
    }
}