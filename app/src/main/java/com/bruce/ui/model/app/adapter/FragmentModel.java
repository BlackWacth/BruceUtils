package com.bruce.ui.model.app.adapter;

import android.support.v4.app.Fragment;

/**
 * 便于在Adapter中传递Fragment和Title
 * Created by Bruce on 2017/3/14.
 */
public class FragmentModel {
    private Fragment fragment;
    private String title;

    public FragmentModel(Fragment fragment, String title) {
        this.fragment = fragment;
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
