package com.bruce.ui.model.app.async;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.AsyncTaskLoader;
import android.text.SpannableString;

import com.bruce.entity.AppInfo;
import com.bruce.global.C;
import com.bruce.ui.model.app.broadcast.PackageChangedReceiver;
import com.orhanobut.logger.Logger;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 异步加载应用列表
 * Created by Bruce on 2017/3/13.
 */
public class AppListLoader extends AsyncTaskLoader<List<AppInfo>> {

    private PackageManager mPackageManager;
    private int mAppType;
    private PackageChangedReceiver mPackageChangedReceiver;
    private List<AppInfo> mAppInfos;

    public AppListLoader(Context context, int type) {
        super(context);
        mAppType = type;
        mPackageManager = context.getPackageManager();
    }

    @Override
    public List<AppInfo> loadInBackground() {
        Logger.i("loader --> loadInBackground");
        List<PackageInfo> packageInfos;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            packageInfos = mPackageManager.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES | PackageManager.MATCH_DISABLED_COMPONENTS);
        } else {
            packageInfos = mPackageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS);
        }
        List<AppInfo> list = new ArrayList<>();
        AppInfo appInfo;
        for(PackageInfo packageInfo: packageInfos) {
            appInfo = new AppInfo();
            appInfo.setAppName(new SpannableString(packageInfo.applicationInfo.loadLabel(mPackageManager)));
            appInfo.setPckName(new SpannableString(packageInfo.packageName));
            appInfo.setVersion(packageInfo.versionName);
            appInfo.setIcon(packageInfo.applicationInfo.loadIcon(mPackageManager));
            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                appInfo.setType(C.USER_APP);
            } else {
                appInfo.setType(C.SYSTEM_APP);
            }
            list.add(appInfo);
        }
        Collections.sort(list, new Comparator<AppInfo>() {
            Collator mCollator = Collator.getInstance();
            @Override
            public int compare(AppInfo lhs, AppInfo rhs) {
                return mCollator.compare(lhs.getAppName().toString(), rhs.getAppName().toString());
            }
        });
        return list;
    }

    @Override
    public void deliverResult(List<AppInfo> data) {
        Logger.i("loader --> deliverResult");

        if(isReset()) {
            if(data != null) {
                data.clear();
                data = null;
            }
        }
        mAppInfos = data;
        List<AppInfo> apps = new ArrayList<>();
        if (data != null) {
            if(mAppType == C.USER_APP) {
                for(AppInfo app : data) {
                    if(app.getType() == C.USER_APP) {
                        apps.add(app);
                    }
                }
            }else {
                for(AppInfo app : data) {
                    if(app.getType() == C.SYSTEM_APP) {
                        apps.add(app);
                    }
                }
            }
        }

        if(isStarted()) {
            super.deliverResult(apps);
        }
    }

    @Override
    protected void onStartLoading() {
        Logger.i("loader --> onStartLoading");
        if(mPackageChangedReceiver == null) {
            mPackageChangedReceiver = new PackageChangedReceiver(this);
        }

        if(takeContentChanged() || mAppInfos == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        Logger.i("loader --> onStopLoading");
        cancelLoad();
    }

    @Override
    protected void onReset() {
        Logger.i("loader --> onReset");
        onStopLoading();
        if(mPackageChangedReceiver != null) {
            getContext().unregisterReceiver(mPackageChangedReceiver);
            mPackageChangedReceiver = null;
        }
    }
}
