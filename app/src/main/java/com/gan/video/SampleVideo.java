package com.gan.video;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gan.video.model.SwitchVideoModel;
import com.jiec.basketball.R;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuyu on 2016/12/7.
 * 注意
 * 这个播放器的demo配置切换到全屏播放器
 * 这只是单纯的作为全屏播放显示，如果需要做大小屏幕切换，请记得在这里耶设置上视频全屏的需要的自定义配置
 */

public class SampleVideo extends StandardGSYVideoPlayer {

    private TextView mMoreScale;

    private TextView mSwitchSize;

    private TextView mChangeRotate;

    private TextView mChangeTransform;

    private ImageView refreshPlayerImageView;

    private RelativeLayout rightRelativeLayout;
    private LinearLayout switchLayout;
    private ListView switchListView;

    private List<SwitchVideoModel> mUrlList = new ArrayList<>();

    //记住切换数据源类型
    private int mType = 0;

    private int mTransformSize = 0;

    //数据源
    private int mSourcePosition = 0;

    private String mTypeText = "标准";

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public SampleVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public SampleVideo(Context context) {
        super(context);
    }

    public SampleVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        initView();
    }

    public ImageView getRefreshPlayerImageView() {
        return refreshPlayerImageView;
    }

    private void initView() {
        mMoreScale = (TextView) findViewById(R.id.moreScale);//视频比例
        mSwitchSize = (TextView) findViewById(R.id.switchSize);//标准，高清
        mChangeRotate = (TextView) findViewById(R.id.change_rotate);//旋转画面
        mChangeTransform = (TextView) findViewById(R.id.change_transform);//旋转镜头

        rightRelativeLayout = findViewById(R.id.right_layout);
        switchLayout = findViewById(R.id.switch_layout);
        switchListView = findViewById(R.id.switch_list);

        refreshPlayerImageView = findViewById(R.id.refreshPlayer);

        //切换画面比例
        mMoreScale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHadPlay) {
                    return;
                }
                if (mType == 0) {
                    mType = 1;
                } else if (mType == 1) {
                    mType = 2;
                } else if (mType == 2) {
                    mType = 3;
                } else if (mType == 3) {
                    mType = 4;
                } else if (mType == 4) {
                    mType = 0;
                }
                resolveTypeUI();
            }
        });

        //切换视频清晰度
        mSwitchSize.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSwitchDialog();
            }
        });

        //旋转播放角度
        mChangeRotate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHadPlay) {
                    return;
                }
                if ((mTextureView.getRotation() - mRotate) == 270) {
                    mTextureView.setRotation(mRotate);
                    mTextureView.requestLayout();
                } else {
                    mTextureView.setRotation(mTextureView.getRotation() + 90);
                    mTextureView.requestLayout();
                }
            }
        });

        //镜像旋转
        mChangeTransform.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHadPlay) {
                    return;
                }
                if (mTransformSize == 0) {
                    mTransformSize = 1;
                } else if (mTransformSize == 1) {
                    mTransformSize = 2;
                } else if (mTransformSize == 2) {
                    mTransformSize = 0;
                }
                resolveTransform();
            }
        });


    }

    /**
     * 需要在尺寸发生变化的时候重新处理
     */
    @Override
    public void onSurfaceSizeChanged(Surface surface, int width, int height) {
        super.onSurfaceSizeChanged(surface, width, height);
        resolveTransform();
    }

    @Override
    public void onSurfaceAvailable(Surface surface) {
        super.onSurfaceAvailable(surface);
        resolveRotateUI();
        resolveTransform();
    }

    /**
     * 处理镜像旋转
     * 注意，暂停时
     */
    protected void resolveTransform() {
        switch (mTransformSize) {
            case 1: {
                Matrix transform = new Matrix();
                transform.setScale(-1, 1, mTextureView.getWidth() / 2, 0);
                mTextureView.setTransform(transform);
                mChangeTransform.setText("左右镜像");
                mTextureView.invalidate();
            }
            break;
            case 2: {
                Matrix transform = new Matrix();
                transform.setScale(1, -1, 0, mTextureView.getHeight() / 2);
                mTextureView.setTransform(transform);
                mChangeTransform.setText("上下镜像");
                mTextureView.invalidate();
            }
            break;
            case 0: {
                Matrix transform = new Matrix();
                transform.setScale(1, 1, mTextureView.getWidth() / 2, 0);
                mTextureView.setTransform(transform);
                mChangeTransform.setText("旋转镜像");
                mTextureView.invalidate();
            }
            break;
        }
    }


    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param title         title
     * @return
     */
    public boolean setUp(List<SwitchVideoModel> url, boolean cacheWithPlay, String title) {
        mUrlList = url;
        initSwitchView();
        return setUp(url.get(mSourcePosition).getUrl(), cacheWithPlay, title);
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @param title         title
     * @return
     */
    public boolean setUp(List<SwitchVideoModel> url, boolean cacheWithPlay, File cachePath, String title) {
        mUrlList = url;
        initSwitchView();
        return setUp(url.get(mSourcePosition).getUrl(), cacheWithPlay, cachePath, title);
    }

    @Override
    public int getLayoutId() {
        return R.layout.sample_video;
    }


    /**
     * 全屏时将对应处理参数逻辑赋给全屏播放器
     *
     * @param context
     * @param actionBar
     * @param statusBar
     * @return
     */
    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        SampleVideo sampleVideo = (SampleVideo) super.startWindowFullscreen(context, actionBar, statusBar);
        sampleVideo.mSourcePosition = mSourcePosition;
        sampleVideo.mType = mType;
        sampleVideo.mTransformSize = mTransformSize;
        sampleVideo.mUrlList = mUrlList;
        sampleVideo.mTypeText = mTypeText;
        //sampleVideo.resolveTransform();
        sampleVideo.resolveTypeUI();
        //sampleVideo.resolveRotateUI();
        //这个播放器的demo配置切换到全屏播放器
        //这只是单纯的作为全屏播放显示，如果需要做大小屏幕切换，请记得在这里耶设置上视频全屏的需要的自定义配置
        //比如已旋转角度之类的等等
        //可参考super中的实现
        return sampleVideo;
    }


    /**
     * 推出全屏时将对应处理参数逻辑返回给非播放器
     *
     * @param oldF
     * @param vp
     * @param gsyVideoPlayer
     */
    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
        if (gsyVideoPlayer != null) {
            SampleVideo sampleVideo = (SampleVideo) gsyVideoPlayer;
            mSourcePosition = sampleVideo.mSourcePosition;
            mType = sampleVideo.mType;
            mTransformSize = sampleVideo.mTransformSize;
            mTypeText = sampleVideo.mTypeText;
            setUp(mUrlList, mCache, mCachePath, mTitle);
            resolveTypeUI();
        }
    }

    /**
     * 旋转逻辑
     */
    private void resolveRotateUI() {
        if (!mHadPlay) {
            return;
        }
        mTextureView.setRotation(mRotate);
        mTextureView.requestLayout();
    }

    /**
     * 显示比例
     * 注意，GSYVideoType.setShowType是全局静态生效，除非重启APP。
     */
    private void resolveTypeUI() {
        if (!mHadPlay) {
            return;
        }
        if (mType == 1) {
            mMoreScale.setText("16:9");
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_16_9);
        } else if (mType == 2) {
            mMoreScale.setText("4:3");
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_4_3);
        } else if (mType == 3) {
            mMoreScale.setText("全屏");
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        } else if (mType == 4) {
            mMoreScale.setText("拉伸全屏");
            GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
        } else if (mType == 0) {
            mMoreScale.setText("默认比例");
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);
        }
        changeTextureViewShowType();
        if (mTextureView != null)
            mTextureView.requestLayout();
        mSwitchSize.setText(mTypeText);
    }


   private void initSwitchView(){

       mSwitchSize.setText(mUrlList.get(0).getName());
        ArrayAdapter adapter = new ArrayAdapter<>(mContext, R.layout.switch_video_dialog_item, mUrlList);
        switchListView.setAdapter(adapter);
        switchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String name = mUrlList.get(position).getName();
                if (mSourcePosition != position) {
//                    int aa = 0;
                    if ((mCurrentState == GSYVideoPlayer.CURRENT_STATE_PLAYING
                            || mCurrentState == GSYVideoPlayer.CURRENT_STATE_PAUSE)) {
                        final String url = mUrlList.get(position).getUrl();
                        onVideoPause();
                        final long currentPosition = mCurrentPosition;
                        getGSYVideoManager().releaseMediaPlayer();
                        cancelProgressTimer();
                        hideAllWidget();
                        getCurPlay().release();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setUp(url, mCache, mCachePath, mTitle);
                                setSeekOnStart(currentPosition);
                                getCurPlay().release();
                                startPlayLogic();
                                cancelProgressTimer();
                                hideAllWidget();
                            }
                        }, 1000);
                        mTypeText = name;
                        mSwitchSize.setText(name);
                        mSourcePosition = position;
                    }

                } else {
                    Toast.makeText(getContext(), "已經是 " + name, Toast.LENGTH_LONG).show();
                }

                mSwitchSize.setVisibility(VISIBLE);
                switchLayout.setVisibility(GONE);
            }
        });
    }

    /**
     * 弹出切换清晰度
     */
    private void showSwitchDialog() {
        if (!mHadPlay) {
            return;
        }
        mSwitchSize.setVisibility(GONE);
        switchLayout.setVisibility(VISIBLE);

//        int[] position = new int[2];
//        rightRelativeLayout.getLocationOnScreen(position);
//        int mWidth = rightRelativeLayout.getWidth();
//        int mHeight = rightRelativeLayout.getHeight();
//
////        SwitchVideoTypeDialog switchVideoTypeDialog = new SwitchVideoTypeDialog(getContext(), mWidth,mHeight);
//        SwitchVideoTypeDialog switchVideoTypeDialog = new SwitchVideoTypeDialog(getContext(), position,  mWidth, mHeight);
//        switchVideoTypeDialog.initList(mUrlList, new SwitchVideoTypeDialog.OnListItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                final String name = mUrlList.get(position).getName();
//                if (mSourcePosition != position) {
//                    if ((mCurrentState == GSYVideoPlayer.CURRENT_STATE_PLAYING
//                            || mCurrentState == GSYVideoPlayer.CURRENT_STATE_PAUSE)) {
//                        final String url = mUrlList.get(position).getUrl();
//                        onVideoPause();
//                        final long currentPosition = mCurrentPosition;
//                        getGSYVideoManager().releaseMediaPlayer();
//                        cancelProgressTimer();
//                        hideAllWidget();
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                setUp(url, mCache, mCachePath, mTitle);
//                                setSeekOnStart(currentPosition);
//                                startPlayLogic();
//                                cancelProgressTimer();
//                                hideAllWidget();
//                            }
//                        }, 500);
//                        mTypeText = name;
//                        mSwitchSize.setText(name);
//                        mSourcePosition = position;
//                    }
//                } else {
//                    Toast.makeText(getContext(), "已经是 " + name, Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        switchVideoTypeDialog.show();
    }

    @Override
    protected void hideAllWidget() {
        super.hideAllWidget();

//        rightRelativeLayout.setVisibility(GONE);
    }

    @Override
    protected void changeUiToNormal() {
        super.changeUiToNormal();
//        rightRelativeLayout.setVisibility(VISIBLE);
    }

    @Override
    protected void setViewShowState(View view, int visibility) {
        super.setViewShowState(view, visibility);
        if (view.getId() == R.id.layout_bottom){
            //rightRelativeLayout.setVisibility(visibility);
            mSwitchSize.setVisibility(visibility);
            //switchLayout.setVisibility(GONE);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        mSwitchSize.setVisibility(GONE);
        switchLayout.setVisibility(GONE);

        return super.onTouch(v, event);

    }

    private GSYVideoPlayer getCurPlay() {
        if (this.getFullWindowPlayer() != null) {
            return  this.getFullWindowPlayer();
        }
        return this;
    }

}
