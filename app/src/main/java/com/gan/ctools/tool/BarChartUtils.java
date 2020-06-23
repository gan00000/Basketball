package com.gan.ctools.tool;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class BarChartUtils {
    private static final String TAG = "BarChartUtils";
    private BarChart bar_chart;
    private XAxis xAxis;
    public BarChartUtils(BarChart chart){
        this.bar_chart = chart;
        initX();
        initY();
    }

    private void initX(){
        bar_chart.setDrawGridBackground(false); // 是否显示表格颜色
//        bar_chart.setTouchEnabled(true); // 设置是否可以触摸
//        bar_chart.setDragEnabled(true);// 是否可以拖拽
//        bar_chart.setScaleEnabled(true);// 是否可以缩放
        bar_chart.getDescription().setText("");//设置不显示右下角的描述
        bar_chart.setDrawBorders(false);//设置无边框
//        bar_chart.setExtraBottomOffset(10); //偏移 为了使x轴的文字显示完全
        bar_chart.setDrawValueAboveBar(true);// 如果设置为true,在条形图上方显示值。如果为false，会显示在顶部下方。
        //设置阴影
        bar_chart.setDrawBarShadow(false);
        Legend legend = bar_chart.getLegend();//设置比例图
        legend.setEnabled(false); //设置是否显示比例图
        legend.setForm(Legend.LegendForm.CIRCLE);//图示 标签的形状。  圆
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        bar_chart.setPinchZoom(true);//设置按比例放缩柱状图
        bar_chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.i(TAG,"---"+e.getX()); //点击第几个柱子
            }

            @Override
            public void onNothingSelected() {

            }
        });

        ////获得x轴对象实例
        xAxis= bar_chart.getXAxis();
        xAxis.setDrawAxisLine(false); //设置显示x轴的线
        xAxis.setDrawGridLines(false); //设置是否显示网格
        xAxis.setGranularity(2f);//设置最小的区间，避免标签的迅速增多
        xAxis.setCenterAxisLabels(true);//设置标签居中
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//数据位于底部
        xAxis.setTextColor(Color.RED);//设置x轴文字颜色

//        xAxis.setAxisMinimum(-0.5f);//设置离左边位置0.5个柱状图的宽度,否则最左侧的柱子会显示半个
    }

    //设置Y轴
    private void initY() {
        YAxis leftY = bar_chart.getAxisLeft();
        YAxis rightY = bar_chart.getAxisRight();
        leftY.setDrawAxisLine(false);//显示左侧y轴的线
        leftY.setTextSize(8);//显示左侧y轴字体大小
        leftY.setLabelCount(0, false);//设置左侧y轴显示文字数量

        //保证Y轴从0开始，不然会上移一点
//        leftY.setAxisMinimum(-20);
//        leftY.setAxisMaximum(20);
        leftY.setStartAtZero(false);
        rightY.setAxisMinimum(-10f);
        rightY.setEnabled(false);//设置y轴关闭
        //设置左侧Y轴上文字的样式
        /*leftY.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int v = (int)value;
                if (v == 0){
                    return "";
                }else{
                    return v+"%";
                }
            }
        });*/


        leftY.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int v = (int)value;
                if (v == 0){
                    return "";
                }else{
                    return v+"%";
                }
            }
        });
    }

    /**
     * 显示柱状图 单个柱子
     * @param yValues 柱子上的数据
     * @param lable
     * @param color
     * @param xValues x轴上显示的数据
     */
    public void showData(List<BarEntry> yValues, String lable, int color, ArrayList<String> xValues){


        //装载显示数据
        BarDataSet barDataSet = new BarDataSet(yValues,lable);
        barDataSet.setColor(color);
        barDataSet.setValueTextSize(14f);//设置柱子上字体大小
        barDataSet.setDrawValues(false);//设置是否显示柱子上的文字
        barDataSet.setHighLightAlpha(37);//设置点击后柱子透明度改变
        //设置柱子上文字的格式
        /*barDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                int v = (int)value;
                return v+"%";
            }
        });*/

//        CustomX customX = new CustomX(xValues);
//        xAxis.setValueFormatter(customX);
        //装载数据
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(barDataSet);
        BarData data = new BarData(dataSets);
        bar_chart.setData(data);
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