package com.jiec.basketball.ui.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.jiec.basketball.R;
import com.jiec.basketball.adapter.ViewPageAdapter;
import com.jiec.basketball.entity.response.HomeResponse;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.ui.rank.RankEastWestFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DataMainFragment extends Fragment {

    @BindView(R.id.tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    Unbinder unbinder;

    public static DataMainFragment newInstance() {
        DataMainFragment fragment = new DataMainFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView(view);
        loadData();
        return view;
    }

    private void initView(View view) {
        ViewPageAdapter adapter = new ViewPageAdapter(getChildFragmentManager());
        adapter.addFragment(PlayerDataFragment.newInstance(), getString(R.string.data_player));
        adapter.addFragment(RankEastWestFragment.newInstance(), getString(R.string.rank_zone_team));
        adapter.addFragment(TeamDataFragment.newInstance(), getString(R.string.data_team));
        mViewpager.setAdapter(adapter);
        mViewpager.setOffscreenPageLimit(1);
        mTabLayout.setViewPager(mViewpager, adapter.getTitles());

    }

    private void loadData() {
        GameApi mService = RetrofitClient.getInstance().create(GameApi.class);
        Observable<HomeResponse> observable = mService.getHomePage();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HomeResponse commResponse) {
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
