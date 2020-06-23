package com.allen.comparsechart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.allen.comparsechart.util.DisplayUtils;
import com.jiec.basketball.R;


/**
 * 作者: allen on 16/1/4.
 */
public class CompareIndicator extends View {
//    private Bitmap mOppositeBitmap;
//    private Bitmap mApproveBitmap;
    private Context context;
//    private Paint BitmapPaint;
    private Paint mOppositePaint;
    private Paint mApprovePaint;
    private int rightValue = 0;
    private int leftValue = 0;
    private float marginLeft_16dp;
    private float marginTop_12dp;
    private float Start_down_cx;
    private float Start_down_cy;
    private int approveLineColor;
    private int oppositeLineColor;

//    public Bitmap getOppositeBitmap() {
//        return mOppositeBitmap;
//    }
//
//    public void setOppositeBitmap(Bitmap oppositeBitmap) {
//        mOppositeBitmap = oppositeBitmap;
//    }
//
//    public Bitmap getApproveBitmap() {
//        return mApproveBitmap;
//    }
//
//    public void setApproveBitmap(Bitmap approveBitmap) {
//        mApproveBitmap = approveBitmap;
//    }

    public Paint getOppositePaint() {
        return mOppositePaint;
    }

    public void setOppositePaint(Paint oppositePaint) {
        mOppositePaint = oppositePaint;
    }

    public Paint getApprovePaint() {
        return mApprovePaint;
    }

    public void setApprovePaint(Paint approvePaint) {
        mApprovePaint = approvePaint;
    }

    public float getStart_down_cx() {
        return Start_down_cx;
    }

    public void setStart_down_cx(float start_down_cx) {
        Start_down_cx = start_down_cx;
    }

    public float getStart_down_cy() {
        return Start_down_cy;
    }

    public void setStart_down_cy(float start_down_cy) {
        Start_down_cy = start_down_cy;
    }

//    public Paint getBitmapPaint() {
//        return BitmapPaint;
//    }
//
//    public void setBitmapPaint(Paint bitmapPaint) {
//        BitmapPaint = bitmapPaint;
//    }

    public int getOppostiteCount() {
        return rightValue;
    }

    public void setOppostiteCount(int oppostiteCount) {
        this.rightValue = oppostiteCount;
        postInvalidate();
    }
    public void updateView(int leftCount,int rightCount){
        this.leftValue = leftCount;
        this.rightValue =rightCount;
        postInvalidate();
    }

    public int getApproveCount() {
        return leftValue;
    }

    public void setApproveCount(int approveCount) {
        this.leftValue = approveCount;
        postInvalidate();
    }

    public CompareIndicator(Context context) {
        super(context);

        this.context = context;
        init(null, 0);
    }

    public CompareIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs, 0);
    }

    public CompareIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        init(attrs, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.compareindicator);
//        int approveIconId = typedArray.getResourceId(R.styleable.compareindicator_approve_res, R.mipmap.good);
//        int oppositeIconId = typedArray.getResourceId(R.styleable.compareindicator_opposite_res, R.mipmap.bad);
         approveLineColor = typedArray.getColor(R.styleable.compareindicator_approve_line_color, getResources().getColor(R.color.good));
         oppositeLineColor = typedArray.getColor(R.styleable.compareindicator_oppose_line_color, getResources().getColor(R.color.bad));
        float lineWidthDimen = typedArray.getDimension(R.styleable.compareindicator_lineWidth, 6f);
        typedArray.recycle();
//        mOppositeBitmap = BitmapFactory.decodeResource(getResources(), approveIconId);
//        mApproveBitmap = BitmapFactory.decodeResource(getResources(), oppositeIconId);
//        BitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mOppositePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOppositePaint.setColor(oppositeLineColor);
        mApprovePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mApprovePaint.setColor(approveLineColor);
        float lineWidth = DisplayUtils.dp2px(context, lineWidthDimen);
        /**
         * 此处不能开启canvas绘图硬件加速 会导致部分api 显示不正常
         */

        /**
         * 画 第三个圆弧
         */

        /**
         * 画涨直线
         */
        mApprovePaint.setStrokeWidth(lineWidth);
        mApprovePaint.setStrokeCap(Paint.Cap.SQUARE);
        /**
         * 画 跌直线
         */
        mOppositePaint.setStrokeWidth(lineWidth);
        mOppositePaint.setStrokeCap(Paint.Cap.SQUARE);
        /**
         * 画左右涨跌图
         */
//        marginLeft_16dp = DisplayUtils.dp2px(context, 16f);
//        marginTop_12dp = DisplayUtils.dp2px(context, 10f);
//        Start_down_cx = marginLeft_16dp + mOppositeBitmap.getWidth() + DisplayUtils.dp2px(context, 8f);
//        Start_down_cy = marginTop_12dp + mOppositeBitmap.getHeight() / 2;

//        Start_down_cx = marginLeft_16dp  + DisplayUtils.dp2px(context, 8f);
//        Start_down_cy = marginTop_12dp ;


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float end_down_cy = Start_down_cy;
//        float end_up_cx = getWidth() - marginLeft_16dp - mApproveBitmap.getWidth() - DisplayUtils.dip2px(context, 8);
//        float end_up_cx = getWidth() - marginLeft_16dp  - DisplayUtils.dip2px(context, 8);
//        float width = end_up_cx - Start_down_cx - DisplayUtils.dp2px(context, 8);
        float width =  getWidth();
        float leftLineWidth;
        float rightLineWidth;
        if (rightValue != 0 && leftValue != 0) {
            leftLineWidth = width / (rightValue + leftValue) * leftValue;
            rightLineWidth = width / (rightValue + leftValue) * rightValue;
        } else if (rightValue == 0 && leftValue != 0) {
            rightLineWidth = DisplayUtils.dp2px(context, 1);
            leftLineWidth = width - rightLineWidth;

        } else if (rightValue != 0 && leftValue == 0) {
            leftLineWidth = DisplayUtils.dp2px(context, 1);
            rightLineWidth = width - leftLineWidth;
        } else {
            leftLineWidth = width / 2;
            rightLineWidth = width / 2;
        }
        float end_down_cx = Start_down_cx + leftLineWidth;
        /**
         * 画 第二个圆弧
         */
//        float start_up_cx = end_down_cx + DisplayUtils.dp2px(context, 8);
//        canvas.drawBitmap(mOppositeBitmap, marginLeft_16dp, marginTop_12dp, BitmapPaint);
//        canvas.drawBitmap(mApproveBitmap, getWidth() - marginLeft_16dp - mApproveBitmap.getWidth(), marginTop_12dp, BitmapPaint);


        canvas.drawLine(Start_down_cx, Start_down_cy, end_down_cx, Start_down_cy, mOppositePaint);

        canvas.drawLine(end_down_cx, end_down_cy, width, end_down_cy, mApprovePaint);
        /**
         * 画 第四个圆弧
         */
//        mOppositePaint.setTextSize(DisplayUtils.dp2px(context, 12));
//        canvas.drawText(String.valueOf(rightValue), Start_down_cx, Start_down_cy + DisplayUtils.dp2px(context, 18), mOppositePaint);
//        mApprovePaint.setTextSize(DisplayUtils.dp2px(context, 12));
//        canvas.drawText(String.valueOf(leftValue), end_up_cx - Util.calcTextWidth(mApprovePaint, String.valueOf(leftValue)), Start_down_cy + DisplayUtils.dp2px(context, 18), mApprovePaint);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if(widthSpecMode ==MeasureSpec.AT_MOST&&heightSpecMode ==MeasureSpec.AT_MOST){
            setMeasuredDimension(200,200);
        }else if(widthSpecMode ==MeasureSpec.AT_MOST){
            setMeasuredDimension(200,heightMeasureSpec);
        }else if(heightSpecMode ==MeasureSpec.AT_MOST){
            setMeasuredDimension(widthMeasureSpec,200);
        }
    }

}
