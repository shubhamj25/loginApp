package com.example.loginapp.screens.usage.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.example.loginapp.BaseFragment;
import com.example.loginapp.R;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.util.ArrayList;

public class CoLineNbarChartFragment extends Fragment {
    private CombinedChart chart;
    private final int count = 12;
    protected Typeface tfLight;

    protected float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }

    protected final String[] months = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan"
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_combined, container, false);
        tfLight = Typeface.createFromAsset(requireActivity().getAssets(), "HappyMonkey-Regular.ttf");

        ImageView chartPopUp=v.findViewById(R.id.chartMenuPopUp);
        chartPopUp.setOnClickListener(listener->showMenu(chartPopUp));

        chart = v.findViewById(R.id.chart1);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(0f, 10f, 0f, 10f);
        chart.setDrawGridBackground(true);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);
        chart.setPinchZoom(true);
        TextView chartTitle = v.findViewById(R.id.chartTitle);
        TextView chartDesc = v.findViewById(R.id.chartDesc);
        chartTitle.setText(R.string.CombinedChart);
        chartDesc.setText(R.string.CombinedChartDesc);
        chart.getAxisRight().setEnabled(false);

        chart.setDrawValueAboveBar(false);


        // draw bars behind lines
        chart.setDrawOrder(new DrawOrder[]{
                DrawOrder.BAR, DrawOrder.LINE, DrawOrder.LINE
        });

        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxisPosition.TOP);
        xAxis.setAxisMinimum(0f);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setGranularity(1f);
        xAxis.setAxisMaximum(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        CombinedData data = new CombinedData();

        LineData d = new LineData();
        d.addDataSet(generateLineData("Usage 2019", Color.YELLOW,
                requireActivity().getColor(R.color.deepOrange)));
        d.addDataSet(generateLineData("Usage 2020", Color.MAGENTA,
                requireActivity().getColor(R.color.purple_200)));
        data.setData(d);

        data.setData(generateBarData(Color.rgb(23, 197, 255)));
        data.setValueTypeface(tfLight);
        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        TypedValue outValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.themeName, outValue, true);
        if ("dark".equals(outValue.string)) {
            leftAxis.setTextColor(Color.WHITE);
            xAxis.setTextColor(Color.WHITE);
            l.setTextColor(Color.WHITE);
            chart.setGridBackgroundColor(Color.parseColor("#50888888"));
        } else {
            chart.setGridBackgroundColor(requireActivity().getColor(R.color.whiteSmoke));
        }

        chart.setData(data);
        chart.setVisibleXRange(0, 5);
        chart.invalidate();
        chart.animateY(1500);
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private LineDataSet generateLineData(String label, int lineColor, int circleColor) {
        ArrayList<Entry> entries1 = new ArrayList<>();
        for (int index = 0; index < count; index++) {
            entries1.add(new Entry(index + 0.5f, getRandom(1500, 100)));
        }

        LineDataSet set = new LineDataSet(entries1, label);
        set.setColor(lineColor);
        set.setLineWidth(2.5f);
        set.setCircleColor(circleColor);
        set.setCircleRadius(5f);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.BLACK);
        TypedValue outValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.themeName, outValue, true);
        if ("dark".equals(outValue.string)) {
            set.setValueTextColor(Color.WHITE);
        }
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        return set;
    }

    private BarData generateBarData(int barColor) {

        ArrayList<BarEntry> entries1 = new ArrayList<>();

        for (int index = 0; index < count; index++) {
            entries1.add(new BarEntry(index + 0.5f, getRandom(1500, 100)));
        }

        BarDataSet set1 = new BarDataSet(entries1, "Usage 2021");
        set1.setColor(barColor);
        set1.setValueTextColor(Color.WHITE);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        TypedValue outValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.themeName, outValue, true);
        if ("dark".equals(outValue.string)) {
            set1.setValueTextColor(Color.WHITE);
        }
        return new BarData(set1);
    }


    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.setOnMenuItemClickListener(this::onOptionsItemSelected);
        popup.inflate(R.menu.combined);
        popup.show();
    }


    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionToggleLineValues: {
                for (IDataSet set : chart.getData().getDataSets()) {
                    if (set instanceof LineDataSet)
                        set.setDrawValues(!set.isDrawValuesEnabled());
                }

                chart.invalidate();
                break;
            }
            case R.id.actionToggleBarValues: {
                for (IDataSet set : chart.getData().getDataSets()) {
                    if (set instanceof BarDataSet)
                        set.setDrawValues(!set.isDrawValuesEnabled());
                }

                chart.invalidate();
                break;
            }
            case R.id.actionRemoveDataSet: {
                int rnd = (int) getRandom(chart.getData().getDataSetCount(), 0);
                chart.getData().removeDataSet(chart.getData().getDataSetByIndex(rnd));
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
                chart.invalidate();
                break;
            }
        }
        return true;
    }
}