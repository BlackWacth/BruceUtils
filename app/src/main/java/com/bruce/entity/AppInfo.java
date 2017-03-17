package com.bruce.entity;

import android.graphics.drawable.Drawable;
import android.text.SpannableString;

/**
 * APP信息
 * Created by Bruce on 2017/3/13.
 */
public class AppInfo {

    private Drawable icon;
    private SpannableString appName;
    private SpannableString pckName;
    private String version;
    private String apkPath;
    private int type;

    public AppInfo() {
    }

    public AppInfo(Drawable icon, SpannableString appName, SpannableString pckName, String version, String apkPath, int type) {
        this.icon = icon;
        this.appName = appName;
        this.pckName = pckName;
        this.version = version;
        this.apkPath = apkPath;
        this.type = type;
    }

    public SpannableString getAppName() {
        return appName;
    }

    public void setAppName(SpannableString appName) {
        this.appName = appName;
    }

    public SpannableString getPckName() {
        return pckName;
    }

    public void setPckName(SpannableString pckName) {
        this.pckName = pckName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "icon=" + icon +
                ", appName=" + appName +
                ", pckName=" + pckName +
                ", version='" + version + '\'' +
                ", apkPath='" + apkPath + '\'' +
                ", type=" + type +
                '}';
    }
}
