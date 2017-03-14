package com.bruce.ui.model.app;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bruce.R;
import com.bruce.global.C;
import com.bruce.ui.model.app.adapter.AppViewPagerAdapter;
import com.bruce.ui.model.app.adapter.FragmentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * App工具首页
 */
public class AppsHomeFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public AppsHomeFragment() {
    }

    public static AppsHomeFragment newInstance() {
        return new AppsHomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apps_home, container, false);
        mTabLayout = (TabLayout) view.findViewById(R.id.tl_apps_home_tab_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_apps_home_view_pager);
        initViewPager();
        return view;
    }

    private void initViewPager() {
        List<FragmentModel> models = new ArrayList<>();
        models.add(new FragmentModel(AppListFragment.newInstance(C.USER_APP), "用户应用"));
        models.add(new FragmentModel(AppListFragment.newInstance(C.SYSTEM_APP), "系统应用"));
        AppViewPagerAdapter appViewPagerAdapter = new AppViewPagerAdapter(getFragmentManager(), models);
        mViewPager.setAdapter(appViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
