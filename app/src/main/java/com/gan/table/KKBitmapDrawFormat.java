package com.gan.table;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.draw.IDrawFormat;

/**
 * Created by huang on 2017/10/30.
 * Bitmap绘制格式化
 */

public abstract class KKBitmapDrawFormat<T> implements IDrawFormat<T> {

    private int imageWidth;
    private int imageHeight;
    protected Rect imgRect;
    protected Rect drawRect;

    private int bitMapDrawPadding;


    public KKBitmapDrawFormat(int imageWidth, int imageHeight, int drawPadding) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.bitMapDrawPadding = drawPadding;
        imgRect = new Rect();
        drawRect = new Rect();
    }



    @Override
    public int measureWidth(Column<T>column,int position, TableConfig config) {
        return  imageWidth;
    }

    @Override
    public int measureHeight(Column<T> column,int position, TableConfig config) {

        return imageHeight;
    }

    /**
     * 获取bitmap
     * @param t 数据
     * @param value 值
     * @param position 位置
     * @return Bitmap 占位图
     */
    protected abstract Bitmap getBitmap(T t,String value, int position);

    /**
     *
     * @param c
     * @param rect 图片左边的位置rect
     * @param cellInfo
     * @param config
     */

    @Override
    public void draw(Canvas c, Rect rect, CellInfo<T> cellInfo ,TableConfig config) {
        Paint paint = config.getPaint();
        Bitmap bitmap =(cellInfo == null
                ? getBitmap(null, null, 0)
                : getBitmap(cellInfo.data, cellInfo.value, cellInfo.row));
        if(bitmap != null) {
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            imgRect.set(0,0,width,height);
            float scaleX = (float)width/imageWidth;
            float scaleY = (float)height/imageHeight;
            if(scaleX >1 || scaleY >1){
                if(scaleX > scaleY){
                    width = (int) (width/scaleX);
                    height = imageHeight;
                }else{
                    height = (int) (height/scaleY);
                    width = imageWidth;
                }
            }
            width= (int) (width*config.getZoom());
            height = (int) (height*config.getZoom());
            int disX= (rect.right-rect.left-width)/2;
            int disY= (rect.bottom-rect.top-height)/2;
            drawRect.left = rect.right + bitMapDrawPadding;
            drawRect.top = rect.top+ disY;

            drawRect.right = drawRect.left + width;

            drawRect.bottom = rect.bottom - disY;
            c.drawBitmap(bitmap, imgRect, drawRect, paint);
        }
    }


    public Rect getDrawRect() {
        return drawRect;
    }

    public void setDrawRect(Rect drawRect) {
        this.drawRect = drawRect;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }
}
