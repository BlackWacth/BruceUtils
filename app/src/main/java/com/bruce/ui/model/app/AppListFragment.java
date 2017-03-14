package com.bruce.ui.model.app;


import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bruce.R;
import com.bruce.entity.AppInfo;
import com.bruce.global.C;
import com.bruce.ui.model.app.adapter.AppRecyclerAdapter;
import com.bruce.ui.model.app.async.AppListLoader;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * App列表展示界面
 */
public class AppListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<AppInfo>>{

    private Context mContext;
    private int mAppType;
    private int mColor;
    private ClipboardManager mClipboardManager;
    private ContentLoadingProgressBar mLoadingProgressBar;
    private RecyclerView mRecyclerView;
    private AppRecyclerAdapter mAppRecyclerAdapter;

    private List<AppInfo> mAppInfos;

    public AppListFragment() {
    }

    public static AppListFragment newInstance(int appType) {
        AppListFragment fragment = new AppListFragment();
        Bundle args = new Bundle();
        args.putInt(C.APP_TYPE, appType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAppType = getArguments().getInt(C.APP_TYPE, C.USER_APP);
        }
        mColor = getResources().getColor(R.color.colorAccent);
        mClipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_app_list_recycler_view);
        mLoadingProgressBar = (ContentLoadingProgressBar) view.findViewById(R.id.clpb_app_list_progress_bar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        if(mAppInfos == null) {
            mAppInfos = new ArrayList<>();
        }
        mAppRecyclerAdapter = new AppRecyclerAdapter(mContext, mAppInfos);
        mRecyclerView.setAdapter(mAppRecyclerAdapter);
        //异步加载APK信息
        getLoaderManager().initLoader(0x111, null, this);
        return view;
    }

    @Override
    public Loader<List<AppInfo>> onCreateLoader(int id, Bundle args) {
        Logger.i("Fragment onCreateLoader");
        mLoadingProgressBar.show();
        return new AppListLoader(mContext, mAppType);
    }

    @Override
    public void onLoadFinished(Loader<List<AppInfo>> loader, List<AppInfo> data) {
        Logger.i("Fragment onLoadFinished");
        mLoadingProgressBar.hide();
        mAppInfos.clear();
        mAppInfos.addAll(data);
        mAppRecyclerAdapter.notifyAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<AppInfo>> loader) {
        Logger.i("Async onLoaderReset");
    }
}
