package com.jiec.basketball.ui;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.LogUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseActivity;
import com.jiec.basketball.core.ServerTimeManager;
import com.jiec.basketball.core.UpdateManager;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.entity.response.TimeResponse;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.ui.data.DataMainFragment;
import com.jiec.basketball.ui.film.FilmListFragment;
import com.jiec.basketball.ui.game.GameMainFragment;
import com.jiec.basketball.ui.mine.MineActivity;
import com.jiec.basketball.ui.news.NewsListFragment;
import com.jiec.basketball.ui.news.detail.DetaillWebActivity;
import com.jiec.basketball.ui.search.SearchActivity;
import com.jiec.basketball.utils.EmptyUtils;
import com.jiec.basketball.utils.EventBusEvent;
import com.jiec.basketball.utils.EventBusUtils;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.wangcj.common.widget.CircleSImageView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import kr.co.namee.permissiongen.PermissionGen;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.jiec.basketball.core.BallApplication.userInfo;
import static com.jiec.basketball.utils.ConstantUtils.EVENT_LOGIN;
import static com.jiec.basketball.utils.ConstantUtils.EVENT_LOGIN_OUT;
import static com.jiec.basketball.utils.ConstantUtils.EVENT_NOTIFICATION;

/**
 * Description : 主界面
 * Author : jiec
 * Date   : 17-1-6
 */
public class MainActivity extends BaseActivity {


    private static final int[] TITLES = {R.string.tab_fight, R.string.tab_news, R.string.tab_films,
                    R.string.tab_data};
    private static final int[] UNSELECTED_ICONS = {R.drawable.icon_fight, R.drawable.icon_news,
            R.drawable.icon_film, R.drawable.icon_data, R.drawable.icon_rank};
    private static final int[] SELECTED_ICONS = {R.drawable.icon_fight_pressed, R.drawable.icon_news_pressed,
            R.drawable.icon_film_pressed, R.drawable.icon_data_pressed, R.drawable.icon_rank_pressed};

    private CommonTabLayout mTabLayout;
    private CircleSImageView ivMine;

    private Button start_search_btn;

    private ArrayList<Fragment> mFragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(userInfo == null){
            //用户信息为空调用自动登录
            UserManager.instance().autoLogin();
        }
        initFragment();
        initView();
        getServerTime();
        checkPermission();


    }

    /**
     * 6.0以上系统检测运行时权限
     */
    private void checkPermission() {
        PermissionGen.with(MainActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBusUtils.registerEvent(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unRegisteredEvent(this);
    }

    private void getServerTime() {
        GameApi service = RetrofitClient.getInstance().create(GameApi.class);
        service.getServerTime().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TimeResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(TimeResponse timeResponse) {
                        if (timeResponse != null && timeResponse.getTime() > 0) {
                            ServerTimeManager.setServertimeOffset(timeResponse.getTime());
                        }
                    }
                });
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(GameMainFragment.newInstance());
        mFragments.add(NewsListFragment.newInstance());
        mFragments.add(FilmListFragment.newInstance());
        mFragments.add(DataMainFragment.newInstance());
       // mFragments.add(RankMainFragment.newInstance());
    }


    private void initView() {

        start_search_btn = findViewById(R.id.start_search_btn);
        start_search_btn.setVisibility(View.GONE);
        start_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));

            }
        });

        mTabLayout = (CommonTabLayout) findViewById(R.id.tab_layout);

        ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();

        for (int i = 0; i < TITLES.length; i++) {
            tabEntities.add(createTabEntity(getString(TITLES[i]), SELECTED_ICONS[i], UNSELECTED_ICONS[i]));
        }

        mTabLayout.setTabData(tabEntities, this, R.id.fl_layout, mFragments);

        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 1 || position == 2){
                    start_search_btn.setVisibility(View.VISIBLE);
                }else {
                    start_search_btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });


         ivMine = (CircleSImageView) findViewById(R.id.iv_mine);
        ImageLoaderUtils.display(this, ivMine, EmptyUtils.emptyOfObject(userInfo) ? " " : userInfo.user_img,
                R.drawable.img_default_head, R.drawable.img_default_head);
        ivMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MineActivity.class));
            }
        });

        new UpdateManager(MainActivity.this).checkUpdate();
    }

    /**
     * 默认tab图标
     *
     * @param title
     * @param selectIcon
     * @param unSelectIconId
     * @return
     */
    private CustomTabEntity createTabEntity(final String title, final int selectIcon, final int unSelectIconId) {
        return new CustomTabEntity() {
            @Override
            public String getTabTitle() {
                return title;
            }

            @Override
            public void setSelectedIcon(ImageView view) {
                view.setImageResource(selectIcon);
            }

            @Override
            public void setUnSelectedIcon(ImageView view) {
                view.setImageResource(unSelectIconId);
            }

        };
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEvent event) {
        switch (event.status){
            case EVENT_LOGIN:
                ImageLoaderUtils.display(this, ivMine, EmptyUtils.emptyOfString(userInfo.user_img) ? " " : userInfo.user_img,
                        R.drawable.img_default_head, R.drawable.img_default_head);
                break;

            case EVENT_LOGIN_OUT:
                ivMine.setImageResource(R.drawable.img_default_head);
                break;

            case EVENT_NOTIFICATION:
                String postId = (String) event.data;
                LogUtils.e(postId+"消息通知Id");
                DetaillWebActivity.show(MainActivity.this, postId, 10);
                break;
        }
    }


}
