package com.jiec.basketball.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;

public class ImageUtil {

    /**
     *
     * @param bitmap
     * @param maxSize 设置压缩的到的最大值  单位 kb
     * @return
     */
    public static byte[] compressBitmap(Bitmap bitmap, float maxSize) {
        if (bitmap == null) {
            return null;//如果图片本身的大小已经小于这个大小了，就没必要进行压缩
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//如果签名是png的话，则不管quality是多少，都不会进行质量的压缩
        Lg.i("压缩前大小:" + baos.toByteArray().length / 1024);
        int quality = 100;
        while (baos.toByteArray().length / 1024f > maxSize && quality > 0) {
            quality = quality - 5;// 每次都减少4
            if (quality <= 0) {
                break;
            }
            baos.reset();// 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        Lg.i("压缩后大小:" + baos.toByteArray().length / 1024);
        return baos.toByteArray();
    }

    public static byte[] compressImage(String imagePath, float maxSize){

        if (StringUtils.isEmpty(imagePath)){
            return null;
        }
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inPreferredConfig = Bitmap.Config.RGB_565;

        Bitmap srcPicBitmap = BitmapFactory.decodeFile(imagePath, options2);
        if(srcPicBitmap == null) {
            return null;
        }

        Log.e("ccLog","newShotPicBitmap is not null");

        Log.i("ccLog","压缩前图片的大小" + (srcPicBitmap.getByteCount() / 1024 / 1024)
                + "M宽度为" + srcPicBitmap.getWidth() + "高度为" + srcPicBitmap.getHeight());

        Matrix matrix = new Matrix();
        if (srcPicBitmap.getWidth() > 1024 && srcPicBitmap.getHeight() > 1024){//图片尺寸不同设置缩放比例不一样
            matrix.setScale(0.5f, 0.5f);
        }else if (srcPicBitmap.getWidth() > 720 || srcPicBitmap.getHeight() > 720){
            matrix.setScale(0.7f, 0.7f);
        }else {
            matrix.setScale(0.9f, 0.9f);
        }
        Bitmap tempBitmap = Bitmap.createBitmap(srcPicBitmap, 0, 0, srcPicBitmap.getWidth(),
                srcPicBitmap.getHeight(), matrix, true);

        Log.i("ccLog","压缩start");
        byte[] compressBitmapData = ImageUtil.compressBitmap(tempBitmap, maxSize);
        Log.i("ccLog","压缩end");

        return compressBitmapData;

    }
}
