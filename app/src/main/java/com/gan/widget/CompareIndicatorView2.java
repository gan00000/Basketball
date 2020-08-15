package com.gan.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.allen.comparsechart.util.DisplayUtils;
import com.jiec.basketball.R;


/**
 * gan
 */
public class CompareIndicatorView2 extends View {

    private Context context;
    private Paint leftLinePaint;//左边线的画笔
    private Paint rightLinePaint;//右边线的画笔
    private  Paint textPaint;

    private int rightValue = 0;
    private int leftValue = 0;

    private int leftLineColor;
    private int leftLineBackgroundColor;

    private int rightLineColor;
    private int rightLineBackgroundColor;

    private int lessCountColor = 0;
    private int bigCountColor = 0;

    private boolean isLineBackgroundColor = true;
    private int lineMarginWidth;

    public int getLessCountColor() {
        return lessCountColor;
    }

    public void setLessCountColor(int lessCountColor) {
        this.lessCountColor = lessCountColor;
    }

    public int getBigCountColor() {
        return bigCountColor;
    }

    public void setBigCountColor(int bigCountColor) {
        this.bigCountColor = bigCountColor;
    }

    public boolean isLineBackgroundColor() {
        return isLineBackgroundColor;
    }

    public void setLineBackgroundColor(boolean lineBackgroundColor) {
        isLineBackgroundColor = lineBackgroundColor;
    }

    public void updateView(int leftCount, int rightCount){
        this.leftValue = leftCount;
        this.rightValue =rightCount;
        postInvalidate();
    }


    public CompareIndicatorView2(Context context) {
        super(context);

        this.context = context;
        init(null, 0);
    }

    public CompareIndicatorView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs, 0);
    }

    public CompareIndicatorView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        init(attrs, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.compareindicator);
        leftLineColor = typedArray.getColor(R.styleable.compareindicator_left_line_color, getResources().getColor(R.color.c_608FD4_ke));
        rightLineColor = typedArray.getColor(R.styleable.compareindicator_right_line_color, getResources().getColor(R.color.c_F35930_zhu));

        leftLineBackgroundColor = typedArray.getColor(R.styleable.compareindicator_left_line_bg_color, getResources().getColor(R.color.gray_light));
        rightLineBackgroundColor = typedArray.getColor(R.styleable.compareindicator_right_line_bg_color, getResources().getColor(R.color.gray_light));
        float lineWidthDimen = typedArray.getDimension(R.styleable.compareindicator_lineWidth, 4f);
        float lineMarginWidthDimen = typedArray.getDimension(R.styleable.compareindicator_lineMarginWidth, 0);
        typedArray.recycle();

        //左边线
        leftLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        leftLinePaint.setColor(leftLineColor);

        //右边线
        rightLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rightLinePaint.setColor(rightLineColor);
        float lineWidth = DisplayUtils.dp2px(context, lineWidthDimen);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(14);

        rightLinePaint.setStrokeWidth(lineWidth);
        rightLinePaint.setStrokeCap(Paint.Cap.SQUARE);

        leftLinePaint.setStrokeWidth(lineWidth);
        leftLinePaint.setStrokeCap(Paint.Cap.SQUARE);

        if (lineMarginWidthDimen > 0){
            lineMarginWidth = (int) DisplayUtils.dp2px(context, lineMarginWidthDimen);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width =  getWidth();

        float leftLineBgWidth = (width - lineMarginWidth) / 2;
        float rightLineBgWidth = leftLineBgWidth;

        float leftLineWidth;
        float rightLineWidth;

        if (rightValue != 0 && leftValue != 0) {
            leftLineWidth = leftLineBgWidth / (rightValue + leftValue) * leftValue;
            rightLineWidth = rightLineBgWidth / (rightValue + leftValue) * rightValue;
        } else if (rightValue == 0 && leftValue != 0) {
            rightLineWidth = 0;
            leftLineWidth = leftLineBgWidth;

        } else if (rightValue != 0 && leftValue == 0) {
            leftLineWidth = rightLineBgWidth;
            rightLineWidth = 0;
        } else {
            leftLineWidth = 0;
            rightLineWidth = 0;
        }

        leftLinePaint.setColor(leftLineBackgroundColor);
        rightLinePaint.setColor(rightLineBackgroundColor);

        int lineY = getHeight() / 2;

//        canvas.drawText("xxd球队", leftLineBgWidth / 2, getHeight() / 3, textPaint);
        //居中显示
        if (isLineBackgroundColor) {
            canvas.drawLine(0, lineY, leftLineBgWidth, lineY, leftLinePaint);
            canvas.drawLine(leftLineBgWidth + lineMarginWidth , lineY, width, lineY, leftLinePaint);
        }

        if (bigCountColor != 0 && lessCountColor != 0){

            if (leftLineWidth > rightLineWidth){

                leftLinePaint.setColor(bigCountColor);
                rightLinePaint.setColor(lessCountColor);
            }else{
                leftLinePaint.setColor(lessCountColor);
                rightLinePaint.setColor(bigCountColor);
            }

        }else{

            leftLinePaint.setColor(leftLineColor);
            rightLinePaint.setColor(rightLineColor);
        }

        if (leftLineWidth > 0) {
            canvas.drawLine(leftLineBgWidth - leftLineWidth, lineY, leftLineBgWidth, lineY, leftLinePaint);
        }
        if (rightLineWidth > 0){

            canvas.drawLine(leftLineBgWidth + lineMarginWidth , lineY, leftLineBgWidth + lineMarginWidth + rightLineWidth, lineY, rightLinePaint);
        }

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
