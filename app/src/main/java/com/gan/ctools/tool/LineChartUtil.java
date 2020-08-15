package com.gan.ctools.tool;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jiec.basketball.R;

import java.util.ArrayList;
import java.util.List;

public class LineChartUtil {
    private static final String TAG = "BarChartUtils";
    private LineChart chart;
    private XAxis xAxis;
    private Context mContext;
    public LineChartUtil(Context mContext, LineChart chart){
        this.chart = chart;
        this.mContext = mContext;
        initChart(chart);
    }

    private void initChart(LineChart chart) {

        {
            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(false);

            // set listeners
//        chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);

            // create marker to display box when values are selected
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

            // Set the marker to the chart
//        mv.setChartView(chart);
//        chart.setMarker(mv);

            // enable scaling and dragging
            chart.setDragEnabled(false);
            chart.setScaleEnabled(false);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置XAxis应该出现的位置。可以选择TOP，BOTTOM，BOTH_SIDED，TOP_INSIDE或者BOTTOM_INSIDE。
//            xAxis.setDrawGridLines(true);//设置为true绘制网格线。
//            xAxis.setDrawLabels(true);//设置为true打开绘制轴的标签。
            xAxis.setGranularity(1f); // only intervals of 1 day //设置最小的区间，避免标签的迅速增多
            xAxis.setCenterAxisLabels(true);
            // vertical grid lines
//            xAxis.enableGridDashedLine(10f, 10f, 0f);
//            xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
            xAxis.setAxisMinimum(0f);
            xAxis.setAxisMaximum(48 * 60);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {


                    if (value % (12 * 60) == 0){
                        int xxv = (int)value / (12 * 60);
                        return "第" + xxv + "节";
                    }else{
                        return "";
                    }

                }
            });
        }

        YAxis yAxis_left;
        {   // // Y-Axis Style // //
            yAxis_left = chart.getAxisLeft();

            // horizontal grid lines
//            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis_left.setAxisMaximum(120f);
            yAxis_left.setAxisMinimum(0f);
        }

        YAxis yAxis_right;
        {
            yAxis_right = chart.getAxisRight();
            yAxis_right.setAxisMinimum(0f);
            yAxis_right.setDrawGridLines(false);
            yAxis_right.setDrawLabels(false);
            yAxis_right.setDrawAxisLine(true);
//            yAxis_right.setEnabled(false);
//            yAxis_right.enableGridDashedLine(10f, 10f, 0f);
            yAxis_right.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return "";
                }
            });
        }

       /* {   // // Create Limit Lines // //
            LimitLine llXAxis = new LimitLine(9f, "Index 10");
            llXAxis.setLineWidth(4f);
            llXAxis.enableDashedLine(10f, 10f, 0f);
            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            llXAxis.setTextSize(10f);
//            llXAxis.setTypeface(tfRegular);

            LimitLine ll1 = new LimitLine(150f, "Upper Limit");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);
//            ll1.setTypeface(tfRegular);

            LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
            ll2.setLineWidth(4f);
            ll2.enableDashedLine(10f, 10f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            ll2.setTextSize(10f);
//            ll2.setTypeface(tfRegular);

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true);
            xAxis.setDrawLimitLinesBehindData(true);

            // add limit lines
            yAxis.addLimitLine(ll1);
            yAxis.addLimitLine(ll2);
            //xAxis.addLimitLine(llXAxis);
        }*/

        // draw points over time
        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);
    }


    public synchronized void showData(List<Entry> values, List<Entry> values2, String lable1, String lable2, int maxGrap, int minGrap){


        YAxis yAxis = chart.getAxisLeft();

        if (maxGrap < 120){
            maxGrap = 120;
        }
        yAxis.setAxisMaximum(maxGrap);
        yAxis.setAxisMinimum(0f);

        LineDataSet set1;
        LineDataSet set2;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();

            set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
            set2.setValues(values2);
            set2.notifyDataSetChanged();

            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();


        } else {
            // create a dataset and give it a type
            set1 = createDataSet(values,lable1);
            set2 = createDataSet(values2,lable2);

            // black lines and points
            set1.setColor(mContext.getResources().getColor(R.color.c_608FD4_ke));
            set1.setCircleColor(mContext.getResources().getColor(R.color.c_608FD4_ke));

            // black lines and points
            set2.setColor(mContext.getResources().getColor(R.color.c_F35930_zhu));
            set2.setCircleColor(mContext.getResources().getColor(R.color.c_F35930_zhu));

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets
            dataSets.add(set2); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
    }

    private LineDataSet createDataSet(List<Entry> values, String lable) {
        LineDataSet set1;
        set1 = new LineDataSet(values, lable);

        set1.setDrawIcons(false);

        // draw dashed line
        set1.enableDashedLine(10f, 5f, 0f);

        // line thickness and point size
        set1.setLineWidth(1.4f);
        set1.setCircleRadius(1.6f);

        // draw points as solid circles
        set1.setDrawCircleHole(false);

        // customize legend entry
//            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//            set1.setFormSize(15.f);

        // text size of values
        set1.setValueTextSize(9f);

        // draw selection line as dashed
        set1.enableDashedHighlightLine(10f, 5f, 0f);

        // set the filled area
        set1.setDrawFilled(false);
            /*set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }*/
        return set1;
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