package com.jiec.basketball.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.jiec.basketball.R;
import com.jiec.basketball.utils.svg.SvgSoftwareLayerSetter;

/**
 * Description : 图片加载工具类
 * Author : lauren
 * Email  : lauren.liuling@gmail.com
 * Blog   : http://www.liuling123.com
 * Date   : 15/12/21
 */
public class ImageLoaderUtils {

    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }

        if (url.endsWith("svg")) {
            loadSvg(context, imageView, url);
            return;
        }

        if (!url.startsWith("http") && url.length() > 50) {
            loadBase64(imageView, url);
            return;
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholder)
                .error(error)
                .priority(Priority.HIGH)
                .centerInside();

        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        display(context, imageView, url, R.drawable.ic_image_loading, R.drawable.ic_image_loadfail);
    }


    private static void loadSvg(Context context, ImageView imageView, String url) {

        RequestBuilder<PictureDrawable> requestBuilder = Glide.with(context)
                .as(PictureDrawable.class)
                .listener(new SvgSoftwareLayerSetter());

        Uri uri = Uri.parse(url);
        requestBuilder.load(uri).into(imageView);

    }

    public static void loadBase64(ImageView imageView, String data) {
        try {
            data = data.replaceAll("\r\n", "");
            data = data.replaceAll("\n", "");

            data = data.substring(data.indexOf(",") + 1);

            byte[] decodedString = Base64.decode(data, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
