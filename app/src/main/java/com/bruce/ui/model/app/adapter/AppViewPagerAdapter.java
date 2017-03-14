package com.bruce.ui.model.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 用户应用与系统应用ViewPager的Adapter
 * Created by Bruce on 2017/3/14.
 */
public class AppViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<FragmentModel> mFragments;

    public AppViewPagerAdapter(FragmentManager fm, List<FragmentModel> list) {
        super(fm);
        mFragments = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle();
    }
}
