package com.jiec.basketball.ui.game.detail;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.gan.video.SampleVideo;
import com.gan.video.model.SwitchVideoModel;
import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseUIActivity;
import com.jiec.basketball.entity.GameLivePost;
import com.jiec.basketball.entity.GamePlayerData;
import com.jiec.basketball.entity.MatchSummary;
import com.jiec.basketball.entity.Matches;
import com.jiec.basketball.ui.game.detail.live.GameLiveFragment;
import com.jiec.basketball.ui.game.detail.statistic.GameStatisticMainFragment;
import com.jiec.basketball.ui.game.detail.summary.GameSummaryFragment;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.wangcj.common.widget.TitleBar;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GameDetailActivity extends BaseUIActivity implements GameDetailContract.View {


    private static final String[] TITLES = { "對陣", "數據統計","文字直播"};
    private static final String TAG = "player";

    GameDetailContract.Presenter mPresenter;
    @BindView(R.id.iv_team_1)
    ImageView mIvTeam1;
    @BindView(R.id.tv_score)
    TextView mTvScore;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    @BindView(R.id.iv_team_2)
    ImageView mIvTeam2;
    @BindView(R.id.tab_layout)
    CommonTabLayout mTabLayout;
    @BindView(R.id.fl_layout)
    FrameLayout mFlLayout;
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.tv_time)
    TextView mTvTime;

    @BindView(R.id.live_player)
    SampleVideo livePlayer;

    @BindView(R.id.live_thumd_layout)
    View liveThumdLayout;

    @BindView(R.id.live_start)
    View liveStart;

    private OrientationUtils orientationUtils;
    private MediaMetadataRetriever mCoverMedia;
    private ImageView coverImageView;
    private boolean isPlay;
    private boolean isPause;
    private boolean isRelease;

    private boolean gameNeedPlay = false;

    private ArrayList<Fragment> mFragments;

    GameLiveFragment mGameLiveFragment;
    GameSummaryFragment mGameSummaryFragment;
    GameStatisticMainFragment mGameStatisticMainFragment;

    public static void show(Context context, String game_id) {
        Intent intent = new Intent(context, GameDetailActivity.class);
        intent.putExtra("game_id", game_id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_detail);
        ButterKnife.bind(this);

        String game_id = getIntent().getStringExtra("game_id");

        //===================播放器start======================
//        String source1 = "http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8";
//        //String source1 = "https://res.exexm.com/cw_145225549855002";
//        String name = "普通";
//        SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, source1);
//
//        String source2 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";
//        String name2 = "清晰";
//        SwitchVideoModel switchVideoModel2 = new SwitchVideoModel(name2, source2);
//
//        list = new ArrayList<>();
//        list.add(switchVideoModel);
//        list.add(switchVideoModel2);


        livePlayer.setEnlargeImageRes(R.drawable.img_full_screen);

        //增加封面
        coverImageView = new ImageView(this);
        coverImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        coverImageView.setImageResource(R.drawable.live_player_bg);
        livePlayer.setThumbImageView(coverImageView);

        resolveNormalVideoUI();

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, livePlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        livePlayer.setIsTouchWiget(true);
        //livePlayer.setIsTouchWigetFull(false);
        //关闭自动旋转
        livePlayer.setRotateViewAuto(false);
        livePlayer.setLockLand(false);

        //打开  实现竖屏全屏动画
        livePlayer.setShowFullAnimation(false);

        livePlayer.setNeedLockFull(true);
        livePlayer.setSeekRatio(1);
        //livePlayer.setOpenPreView(false);
        livePlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //屏蔽，实现竖屏全屏

                //此处需要直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                livePlayer.startWindowFullscreen(GameDetailActivity.this, true, true);

            }
        });

        livePlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);

                //开始播放了才能旋转和全屏
                orientationUtils.setEnable(true);//是否可以旋转和全屏
                isPlay = true;
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }

            @Override
            public void onClickStartError(String url, Object... objects) {
                super.onClickStartError(url, objects);
                Toast.makeText(GameDetailActivity.this,"播放出錯",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                //屏蔽，实现竖屏全屏

                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo();
                }
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                Object title = objects[0];
                SampleVideo fullSampleVideo = (SampleVideo)objects[1];
                fullSampleVideo.getFullscreenButton().setImageResource(R.drawable.img_full_screen);
                fullSampleVideo.getRefreshPlayerImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rePlay();
                    }
                });

                Log.d(TAG,"onEnterFullscreen");
            }
        });

        livePlayer.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                //屏蔽，实现竖屏全屏
                if (orientationUtils != null) {//锁定之后不能旋转和全屏
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });


        //loadFirstFrameCover(source1);

        //===================播放器end======================

        mFragments = new ArrayList<>();

        mGameLiveFragment = GameLiveFragment.newInstance(game_id);//文字直播
        mGameLiveFragment.setGameLiveUpdateListener(new GameLiveFragment.GameLiveUpdateListener() {
            @Override
            public void onUpdate(MatchSummary matchSummary, Matches matches) {

                mGameSummaryFragment.setSummary(matchSummary);

                ImageLoaderUtils.display(GameDetailActivity.this, mIvTeam1, matchSummary.getHomeLogo());
                ImageLoaderUtils.display(GameDetailActivity.this, mIvTeam2, matchSummary.getAwayLogo());
                mTvScore.setText(matchSummary.getHome_pts() + "-" + matchSummary.getAway_pts());

                if (matchSummary.getScheduleStatus().equals("Final")) {
                    mTvStatus.setText(R.string.game_state_final);
                    mTvTime.setVisibility(View.GONE);

                } else if (matchSummary.getScheduleStatus().equals("InProgress")) {
                    mTvStatus.setText("");
                    mTvTime.setVisibility(View.VISIBLE);
                    if (matchSummary.getQuarter().contains("OT")) {
                        mTvTime.setText(matchSummary.getQuarter());
                    } else {
                        mTvTime.setText("第" + matchSummary.getQuarter() + "节\n" + matchSummary.getTime());
                    }

                    gameNeedPlay = true;//需要進行播放

                    mGameLiveFragment.loadVideoLiveData();

                } else {
                    mTvStatus.setText(R.string.game_state_unstart);
                    mTvTime.setVisibility(View.GONE);

                }

                mTitleBar.setTitle(matchSummary.getHomeName() + " vs " + matchSummary.getAwayName());
                mGameLiveFragment.loadVideoLiveData();//放此處測試
            }

            @Override
            public void onUpdateLiveVideo(GameLivePost gameLivePost) {

//                String source1 = "http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8";

                List<String> videoUrls =gameLivePost.getLive_url();

                if (videoUrls == null || videoUrls.isEmpty()){
                    return;
                }

                List<SwitchVideoModel> switchVideoModels = new ArrayList<>();

                for (int i = 0; i < videoUrls.size(); i++) {
                    String videoUrl = videoUrls.get(i);

                    if (StringUtils.isNotEmpty(videoUrl)){
                        String name = "直播" + i + 1;
                        SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, videoUrl);
                        switchVideoModels.add(switchVideoModel);

                    }
                }

                if (switchVideoModels.isEmpty()){
                    return;
                }
                liveThumdLayout.setVisibility(View.GONE);
                livePlayer.setVisibility(View.VISIBLE);
                livePlayer.setUp(switchVideoModels, true, "");
                rePlay();

            }
        });

        mGameSummaryFragment = GameSummaryFragment.newInstance();

        mGameStatisticMainFragment = GameStatisticMainFragment.newInstance(game_id);
        mGameStatisticMainFragment.setOnDataUpdateListener(new GameStatisticMainFragment.OnDataUpdateListener() {
            @Override
            public void onUpdate(List<GamePlayerData> homeData, List<GamePlayerData> awayData) {
                mGameSummaryFragment.refreshData(homeData, awayData);
            }
        });

        mFragments.add(mGameSummaryFragment);//队长
        mFragments.add(mGameStatisticMainFragment);//数据统计
        mFragments.add(mGameLiveFragment);//文字直播

        ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();

        for (int i = 0; i < TITLES.length; i++) {
            tabEntities.add(createTabEntity(TITLES[i]));
        }

        mTabLayout.setTabData(tabEntities, this, R.id.fl_layout, mFragments);

    }

    /**
     * 默认tab图标
     *
     * @param title
     * @return
     */
    private CustomTabEntity createTabEntity(final String title) {
        return new CustomTabEntity() {
            @Override
            public String getTabTitle() {
                return title;
            }

            @Override
            public void setSelectedIcon(ImageView view) {
            }

            @Override
            public void setUnSelectedIcon(ImageView view) {
            }
        };
    }

    @Override
    public GameDetailContract.Presenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new GameDetailPresenter(this);
        }
        return mPresenter;
    }

    @Override
    public void setPresenter(GameDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onBackPressed() {

        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        getCurPlay().onVideoResume();
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRelease = true;
        if (isPlay) {
            getCurPlay().release();
        }
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
        if (mCoverMedia != null) {
            try {
                mCoverMedia.release();
            } catch (IOException e) {

            }
            mCoverMedia = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            livePlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }

    private GSYVideoPlayer getCurPlay() {
        if (livePlayer.getFullWindowPlayer() != null) {
            return  livePlayer.getFullWindowPlayer();
        }
        return livePlayer;
    }


    private void resolveNormalVideoUI() {
        //增加title
        livePlayer.getTitleTextView().setVisibility(View.GONE);
        livePlayer.getBackButton().setVisibility(View.GONE);

//        livePlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        livePlayer.getRefreshPlayerImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rePlay();

            }
        });
    }

    private void rePlay() {

        if (isPlay){
            getCurPlay().release();
            isPlay = false;
            isRelease = true;
//        getCurPlay().onVideoPause();
//        getCurPlay().getGSYVideoManager().releaseMediaPlayer();
        }

        Handler mHandler = new Handler();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getCurPlay().startPlayLogic();
                isPlay = true;
                isRelease = false;
            }
        },500);
    }

//    @Override
//    public LifecycleTransformer getBindToLifecycle() {
//        return bindUntilEvent(FragmentEvent.DESTROY);
//    }
}
