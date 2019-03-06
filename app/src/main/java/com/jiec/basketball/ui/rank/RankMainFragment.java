package com.jiec.basketball.ui.rank;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.jiec.basketball.R;
import com.jiec.basketball.adapter.ViewPageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RankMainFragment extends Fragment {

    @BindView(R.id.tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    Unbinder unbinder;

    public static RankMainFragment newInstance() {
        RankMainFragment fragment = new RankMainFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView(view);
        return view;
    }

    private void initView(View view) {
        ViewPageAdapter adapter = new ViewPageAdapter(getChildFragmentManager());
        adapter.addFragment(RankEastWestFragment.newInstance(), getString(R.string.rank_ew));
        adapter.addFragment(RankZoneFragment.newInstance(), getString(R.string.rank_zone));
        mViewpager.setAdapter(adapter);
        mViewpager.setOffscreenPageLimit(1);
        mTabLayout.setViewPager(mViewpager, adapter.getTitles());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
