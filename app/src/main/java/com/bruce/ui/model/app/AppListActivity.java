package com.bruce.ui.model.app;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.bruce.R;
import com.bruce.global.C;
import com.bruce.ui.model.app.adapter.AppViewPagerAdapter;
import com.bruce.ui.model.app.adapter.FragmentModel;

import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        mViewPager = (ViewPager) findViewById(R.id.vp_app_list_view_pager);
        mToolbar = (Toolbar) findViewById(R.id.tb_app_list_toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tl_app_list_tab_layout);
        mToolbar.setTitle(R.string.title_activity_app_list);
        setSupportActionBar(mToolbar);
        initViewPager();
    }

    private void initViewPager() {
        List<FragmentModel> models = new ArrayList<>();
        models.add(new FragmentModel(AppListFragment.newInstance(C.USER_APP), "用户应用"));
        models.add(new FragmentModel(AppListFragment.newInstance(C.SYSTEM_APP), "系统应用"));
        AppViewPagerAdapter appViewPagerAdapter = new AppViewPagerAdapter(getSupportFragmentManager(), models);
        mViewPager.setAdapter(appViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
