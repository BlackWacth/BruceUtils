package com.bruce.ui.model.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.bruce.ui.model.app.async.AppListLoader;

/**
 * APK安装、卸载改变广播，改变则通知界面重新加载
 * Created by Bruce on 2017/3/13.
 */
public class PackageChangedReceiver extends BroadcastReceiver {

    private final AppListLoader mAppListLoader;

    public PackageChangedReceiver(AppListLoader appListLoader) {
        mAppListLoader = appListLoader;

        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        mAppListLoader.getContext().registerReceiver(this, filter);

        IntentFilter sdFilter = new IntentFilter();
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        mAppListLoader.getContext().registerReceiver(this, sdFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mAppListLoader.onContentChanged();
    }



}
