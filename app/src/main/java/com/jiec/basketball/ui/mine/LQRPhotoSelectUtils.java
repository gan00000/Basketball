package com.jiec.basketball.ui.mine;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;

import com.jiec.basketball.utils.Lg;
import com.yalantis.ucrop.UCrop;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * CSDN_LQR
 * 图片选择工具类
 */
public class LQRPhotoSelectUtils {

    public static final int REQ_TAKE_PHOTO = 10001;
    public static final int REQ_SELECT_PHOTO = 10002;
    public static final int REQ_ZOOM_PHOTO = 10003;

    private AppCompatActivity mActivity;
    //拍照或剪切后图片的存放位置(参考file_provider_paths.xml中的路径)
    private String imgPath;
    //FileProvider的主机名：一般是包名+".fileprovider"，严格上是build.gradle中defaultConfig{}中applicationId对应的值+".fileprovider"
    private String AUTHORITIES = "packageName" + ".fileprovider";
    private boolean mShouldCrop = false;//是否要裁剪（默认不裁剪）
    private Uri mTakePhoneOutputUri = null;
//    private File mInputFile;
//    private File mOutputFile = null;

    //剪裁图片宽高比
    private int mAspectX = 1;
    private int mAspectY = 1;
    //剪裁图片大小
    private int mOutputX = 300;
    private int mOutputY = 300;
    PhotoSelectListener mListener;

    /**
     * 可指定是否在拍照或从图库选取照片后进行裁剪
     * <p>
     * 默认裁剪比例1:1，宽度为800，高度为480
     *
     * @param activity   上下文
     * @param listener   选取图片监听
     * @param shouldCrop 是否裁剪
     */
    public LQRPhotoSelectUtils(AppCompatActivity activity, PhotoSelectListener listener, boolean shouldCrop) {
        mActivity = activity;
        mListener = listener;
        mShouldCrop = shouldCrop;
        AUTHORITIES = activity.getPackageName() + ".fileprovider";
        imgPath = generateImgePath();
    }

    /**
     * 可以拍照或从图库选取照片后裁剪的比例及宽高
     *
     * @param activity 上下文
     * @param listener 选取图片监听
     * @param aspectX  图片裁剪时的宽度比例
     * @param aspectY  图片裁剪时的高度比例
     * @param outputX  图片裁剪后的宽度
     * @param outputY  图片裁剪后的高度
     */
    public LQRPhotoSelectUtils(AppCompatActivity activity, PhotoSelectListener listener, int aspectX, int aspectY, int outputX, int outputY) {
        this(activity, listener, true);
        mAspectX = aspectX;
        mAspectY = aspectY;
        mOutputX = outputX;
        mOutputY = outputY;
    }

    /**
     * 设置FileProvider的主机名：一般是包名+".fileprovider"，严格上是build.gradle中defaultConfig{}中applicationId对应的值+".fileprovider"
     * <p>
     * 该工具默认是应用的包名+".fileprovider"，如项目build.gradle中defaultConfig{}中applicationId不是包名，则必须调用此方法对FileProvider的主机名进行设置，否则Android7.0以上使用异常
     *
     * @param authorities FileProvider的主机名
     */
    public void setAuthorities(String authorities) {
        this.AUTHORITIES = authorities;
    }

    /**
     * 修改图片的存储路径（默认的图片存储路径是SD卡上 Android/data/应用包名/时间戳.jpg）
     *
     * @param imgPath 图片的存储路径（包括文件名和后缀）
     */
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    /**
     * 拍照获取
     */
    public void takePhoto() {

        File imgFile = new File(imgPath);
        if (!imgFile.getParentFile().exists()) {
            imgFile.getParentFile().mkdirs();
        }
        Uri imgUri = null;

        if (Build.VERSION.SDK_INT < 24) {
            // 从文件中创建uri
            imgUri = Uri.fromFile(imgFile);
        }else {
            //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
            imgUri = FileProvider.getUriForFile(mActivity, mActivity.getPackageName() + ".fileprovider", imgFile);

        }

        mTakePhoneOutputUri = imgUri;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        mActivity.startActivityForResult(intent, REQ_TAKE_PHOTO);
    }

    /**
     * 从图库获取
     */
    public void selectPhoto() {
//        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        mActivity.startActivityForResult(intent, REQ_SELECT_PHOTO);
        pickFromGallery();
    }

    private void pickFromGallery() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
                .addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }

        mActivity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_SELECT_PHOTO);
    }

    //测试有些问题，剪裁相册的图片 后不知道什么原因 大小为0b
    @Deprecated
    private void zoomPhoto(File inputFile, File outputFile) {
        File parentFile = outputFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(getImageContentUri(mActivity, inputFile), "image/*");
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            intent.setDataAndType(Uri.fromFile(inputFile), "image/*");
        }
        intent.putExtra("crop", "true");

        //设置剪裁图片宽高比
        intent.putExtra("mAspectX", mAspectX);
        intent.putExtra("mAspectY", mAspectY);

        //设置剪裁图片大小
        intent.putExtra("mOutputX", mOutputX);
        intent.putExtra("mOutputY", mOutputY);

        // 是否返回uri
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        mActivity.startActivityForResult(intent, REQ_ZOOM_PHOTO);
    }

    private void zoomPhotoByUCrop(Uri inputUri) {


        File destinationFile = new File(mActivity.getCacheDir(), "temp" + System.currentTimeMillis() + ".jpg");

        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(90);

        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);

        UCrop.of(inputUri, Uri.fromFile(destinationFile))
                .withAspectRatio(mAspectX, mAspectY)
                .withMaxResultSize(mOutputX, mOutputY)
                .withOptions(options)
                .start(mActivity);

    }

    public void attachToActivityForResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {//剪裁处理
            final Uri resultUri = UCrop.getOutput(data);
            Lg.i("resultUri:" + resultUri);
            if (mListener != null) {
                mListener.onFinish(null, resultUri);
            }
            return;
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Lg.e("cropError:" + cropError.getMessage());
            if (mListener != null) {
                mListener.onFinish(null, null);
            }
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case LQRPhotoSelectUtils.REQ_TAKE_PHOTO://拍照
                    if (mShouldCrop) {//裁剪
                        if (mTakePhoneOutputUri != null && StringUtils.isNotEmpty(mTakePhoneOutputUri.toString())){
                            zoomPhotoByUCrop(mTakePhoneOutputUri);
                        }
                    } else {//不裁剪
                        if (mListener != null) {
                            mListener.onFinish(null, mTakePhoneOutputUri);
                        }
                    }
                   // mTakePhoneOutputUri = null;
                    break;
                case LQRPhotoSelectUtils.REQ_SELECT_PHOTO://图库
                    if (data != null) {
                        Uri sourceUri = data.getData();
//                        String[] proj = {MediaStore.Images.Media.DATA};
//                        Cursor cursor = mActivity.managedQuery(sourceUri, proj, null, null, null);
//                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                        cursor.moveToFirst();
//                        String imgPath = cursor.getString(columnIndex);
//                        mInputFile = new File(imgPath);

                        if (mShouldCrop) {//裁剪
                            zoomPhotoByUCrop(sourceUri);
                        } else {//不裁剪
                            if (mListener != null) {
                                mListener.onFinish(null, sourceUri);
                            }
                        }
                    }
                    break;
//                case LQRPhotoSelectUtils.REQ_ZOOM_PHOTO://裁剪
//                    if (data != null) {
//                        if (mOutputUri != null) {
//                            //删除拍照的临时照片
//                            File tmpFile = new File(imgPath);
//                            if (tmpFile.exists())
//                                tmpFile.delete();
//                            if (mListener != null) {
//                                mListener.onFinish(mOutputFile, mOutputUri);
//                            }
//                        }
//                    }
//                    break;

            }
        }
    }

    /**
     * 安卓7.0裁剪根据文件路径获取uri
     */
    private Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 产生图片的路径，带文件夹和文件名，文件名为当前毫秒数
     */
    private String generateImgePath() {
        return getExternalStoragePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";
    }


    /**
     * 获取SD下的应用目录
     */
    private String getExternalStoragePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        String ROOT_DIR = mActivity.getPackageName();
        sb.append(ROOT_DIR);
        sb.append(File.separator);
        return sb.toString();
    }

    public interface PhotoSelectListener {
        void onFinish(File outputFile, Uri outputUri);
    }
}
