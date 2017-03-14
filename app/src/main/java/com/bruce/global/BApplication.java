package com.bruce.global;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * Created by Bruce on 2017/3/13.
 */

public class BApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("Bruce");
    }
}
