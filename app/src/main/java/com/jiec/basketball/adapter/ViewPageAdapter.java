package com.jiec.basketball.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jiec.basketball.core.BallApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiec on 2017/2/23.
 */

public class ViewPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mFragmentTitles = new ArrayList<>();

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    public void addFragment(Fragment fragment, int titleStringId) {
        addFragment(fragment, BallApplication.getContext().getString(titleStringId));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }

    public String[] getTitles() {
        String[] titles = new String[mFragmentTitles.size()];
        for (int i = 0; i < mFragmentTitles.size(); i++) {
            titles[i] = mFragmentTitles.get(i);
        }
        return titles;
    }
}
