package com.gan.ctools.tool;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jiec.basketball.R;

import java.util.ArrayList;
import java.util.List;

public class CombinedChartUtil {
    private static final String TAG = "BarChartUtils";
    private CombinedChart chart;
    private Context mContext;
    public CombinedChartUtil(Context mContext, CombinedChart chart){
        this.chart = chart;
        this.mContext = mContext;
        initChart(chart);
    }

    private void initChart(CombinedChart chart) {

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
            chart.setPinchZoom(false);

            // draw bars behind lines
            chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                    CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
            });
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置XAxis应该出现的位置。可以选择TOP，BOTTOM，BOTH_SIDED，TOP_INSIDE或者BOTTOM_INSIDE。
            xAxis.setDrawGridLines(false);//设置为true绘制网格线。
            xAxis.setDrawLabels(false);//设置为true打开绘制轴的标签。
            xAxis.setGranularity(1f); // only intervals of 1 day //设置最小的区间，避免标签的迅速增多
            xAxis.setCenterAxisLabels(true);
            // vertical grid lines
//            xAxis.enableGridDashedLine(10f, 10f, 0f);
//            xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
            xAxis.setAxisMinimum(0f);
            xAxis.setAxisMaximum(48 * 60);
            xAxis.setLabelCount(5,true);
            /*xAxis.setValueFormatter(new ValueFormatter() {

                boolean is1 = false;
                boolean is2 = false;
                boolean is3 = false;
                boolean is4 = false;
                @Override
                public String getFormattedValue(float value) {

                    //float visibleXRange = chart.getVisibleXRange();
                    if (!is1 && value == 0){
//                        is1 = true;
                        return "第1節";
                    }else if (!is2 && value == 720){
//                        is2 = true;
                        return "第2節";
                    }else if (!is3 && value == 1440){
//                        is3 = true;
                        return "第3節";
                    }else if (!is4 && value == 2160){
//                        is4 = true;
                        return "第4節";
                    }
                    return "";

                }
            });*/
        }

        YAxis yAxis_left;
        {   // // Y-Axis Style // //
            yAxis_left = chart.getAxisLeft();

            // horizontal grid lines
//            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis_left.setAxisMaximum(120f);
            yAxis_left.setAxisMinimum(0f);
            yAxis_left.setGranularity(30);
//            yAxis_left.setGranularityEnabled(true);
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
//        Legend l = chart.getLegend();
//        // draw legend entries as lines
//        l.setForm(Legend.LegendForm.LINE);

        Legend l = chart.getLegend(); //圖標注釋
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
    }


    public synchronized void showData(List<Entry> values, List<Entry> values2, String lable1, String lable2, int maxGrap, int toCount){

        XAxis xAxis = chart.getXAxis();
        if (toCount > 0){
            int gameSeconds = 48 * 60 + 5 * 60 * toCount;
            xAxis.setAxisMaximum(gameSeconds);
//            xAxis.setLabelCount(5,true);
        }else {
//            xAxis.setLabelCount(5,true);
        }

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
            LineData mLineData = new LineData(dataSets);

            Legend l = chart.getLegend(); //圖標注釋
            l.setWordWrapEnabled(true);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);

            List<LegendEntry> xxl = new ArrayList<>();
            LegendEntry LegendEntry1 = new LegendEntry();
            LegendEntry1.label = lable1;
            LegendEntry1.formColor = mContext.getResources().getColor(R.color.c_608FD4_ke);
            xxl.add(LegendEntry1);

            LegendEntry LegendEntry2 = new LegendEntry();
            LegendEntry2.label = lable2;
            LegendEntry2.formColor = mContext.getResources().getColor(R.color.c_F35930_zhu);
            xxl.add(LegendEntry2);
            l.setCustom(xxl);


            //============================================
            //============================================
            //============================================

             float barWidth = 12 * 60f;
            List<BarEntry> barEntriesValues = new ArrayList<>();
            BarEntry mBarEntry1 = new BarEntry(12 * 60 - barWidth / 2, maxGrap);
            BarEntry mBarEntry2 = new BarEntry(2 * 12 * 60  - barWidth / 2, maxGrap);
            BarEntry mBarEntry3 = new BarEntry(3 * 12 * 60  - barWidth / 2, maxGrap);
            BarEntry mBarEntry4 = new BarEntry(4 * 12 * 60  - barWidth / 2, maxGrap);
            barEntriesValues.add(mBarEntry1);
            barEntriesValues.add(mBarEntry2);
            barEntriesValues.add(mBarEntry3);
            barEntriesValues.add(mBarEntry4);

            List<Integer> colors = new ArrayList<>();
            int endColor1 = ContextCompat.getColor(mContext, R.color.c_33ffffff);
            int endColor2 = Color.TRANSPARENT;
            colors.add(endColor1);
            colors.add(endColor2);
            colors.add(endColor1);
            colors.add(endColor2);

            if (toCount > 0){
                /*for (int i = 0; i < toCount; i++) {
                    BarEntry mBarEntryto = new BarEntry((i * 5 * 60 + 48 * 60)  - 5 * 60 / 2, maxGrap);
                    barEntriesValues.add(mBarEntryto);
                    if ((i + 1) % 2 == 0){
                        colors.add(endColor2);
                    }else{
                        colors.add(endColor1);
                    }

                }*/

                BarEntry mBarEntryto = new BarEntry(5 * 12 * 60  - barWidth / 2, maxGrap);
                barEntriesValues.add(mBarEntryto);
                colors.add(endColor1);
            }


            //装载显示数据
            BarDataSet barDataSet = new BarDataSet(barEntriesValues,"");

            barDataSet.setColors(colors);
            barDataSet.setValueTextSize(14f);//设置柱子上字体大小
            barDataSet.setDrawValues(false);//设置是否显示柱子上的文字
            barDataSet.setDrawIcons(false);
//        barDataSet.setHighLightAlpha(37);//设置点击后柱子透明度改变

            //设置柱子上文字的格式
            barDataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return (int)value + "";
                }
            });

            //装载数据
            ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
            barDataSets.add(barDataSet);
            BarData barData = new BarData(barDataSet);
            barData.setBarWidth(barWidth);//设置树状图的宽度


            // set data
            CombinedData mCombinedData = new CombinedData();
            mCombinedData.setData(barData);
            mCombinedData.setData(mLineData);

            chart.setData(mCombinedData);
            chart.invalidate();
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

}