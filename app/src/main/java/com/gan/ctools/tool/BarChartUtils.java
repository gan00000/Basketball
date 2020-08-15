package com.gan.ctools.tool;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.jiec.basketball.R;

import java.util.ArrayList;
import java.util.List;

public class BarChartUtils {
    private static final String TAG = "BarChartUtils";
    private BarChart bar_chart;
    private XAxis xAxis;
    private Context mContext;
    public BarChartUtils(Context mContext,BarChart chart){
        this.bar_chart = chart;
        this.mContext = mContext;
        initChart(bar_chart);
        initX(bar_chart);
        initY(bar_chart);
    }

    private void initChart(BarChart chart) {

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);// 如果设置为true,在条形图上方显示值。如果为false，会显示在顶部下方。
        chart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setTouchEnabled(false);//禁止触摸放大
        chart.setDragEnabled(false);

        chart.setDrawGridBackground(false);
        chart.setFitBars(true);
        chart.getLegend().setEnabled(false);

//        chart.setExtraLeftOffset(20f);
//        chart.setExtraRightOffset(20f);

//        Legend l = chart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setForm(Legend.LegendForm.SQUARE);
//        l.setFormSize(9f);
//        l.setTextSize(11f);
//        l.setXEntrySpace(4f);
    }

    private void initX(BarChart chart){

        XAxis xAxis = chart.getXAxis();//获得x轴对象实例
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置XAxis应该出现的位置。可以选择TOP，BOTTOM，BOTH_SIDED，TOP_INSIDE或者BOTTOM_INSIDE。
//        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);//设置为true绘制网格线。
        xAxis.setDrawLabels(false);//设置为true打开绘制轴的标签。
        xAxis.setGranularity(1f); // only intervals of 1 day //设置最小的区间，避免标签的迅速增多
//        xAxis.setLabelCount(2);
//        xAxis.setValueFormatter(xAxisFormatter);

//        xAxis.setDrawAxisLine(false); //设置显示x轴的线
//        xAxis.setCenterAxisLabels(true);//设置标签居中
//        xAxis.setTextColor(Color.RED);//设置x轴文字颜色
        xAxis.setAxisMinimum(0);//设置离左边位置0.5个柱状图的宽度,否则最左侧的柱子会显示半个

        xAxis.setEnabled(false);
    }

    //设置Y轴
    private void initY(BarChart chart) {

        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setTypeface(tfLight);
//        leftAxis.setLabelCount(8, false);
//        leftAxis.setValueFormatter(custom);
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        leftAxis.setEnabled(false);

        YAxis rightAxis = chart.getAxisRight();
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setTypeface(tfLight);
//        rightAxis.setLabelCount(8, false);
//        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        rightAxis.setEnabled(false);
    }

    /**
     * 显示柱状图 单个柱子
     * @param yValues 柱子上的数据
     * @param lable
//     * @param color
//     * @param xValues x轴上显示的数据
     */
//    public void showData(List<BarEntry> yValues, String lable, int color, ArrayList<String> xValues){
    public void showData(List<BarEntry> yValues, String lable){

        BarDataSet set;
        if (bar_chart.getData() != null &&
                bar_chart.getData().getDataSetCount() > 0) {
            set = (BarDataSet) bar_chart.getData().getDataSetByIndex(0);
            set.setValues(yValues);
            bar_chart.getData().notifyDataChanged();
            bar_chart.notifyDataSetChanged();
            return;
        }

        //装载显示数据
        BarDataSet barDataSet = new BarDataSet(yValues,lable);
        int endColor4 = ContextCompat.getColor(mContext, R.color.c_608FD4_ke);
        int endColor5 = ContextCompat.getColor(mContext, R.color.c_F35930_zhu);

        barDataSet.setColors(endColor4, endColor5);
        barDataSet.setValueTextSize(14f);//设置柱子上字体大小
        barDataSet.setDrawValues(true);//设置是否显示柱子上的文字
//        barDataSet.setHighLightAlpha(37);//设置点击后柱子透明度改变

        //设置柱子上文字的格式
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int)value + "";
            }
        });

        //装载数据
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(barDataSet);
        BarData data = new BarData(dataSets);
        data.setBarWidth(1.2f);//设置树状图的宽度
        bar_chart.setData(data);
        bar_chart.invalidate();
    }

    class CustomX extends ValueFormatter {

        private ArrayList<String> list;
        public CustomX(ArrayList<String> list ){
            this.list = list;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            int v = (int) value;
            if (v<list.size()){
                return list.get(v);
            }else{
                return "";
            }
        }
    }
}