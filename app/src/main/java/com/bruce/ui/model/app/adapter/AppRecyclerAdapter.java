package com.bruce.ui.model.app.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.R;
import com.bruce.entity.AppInfo;
import com.bruce.expandablelayoutlib.ExpandableLayout;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.List;

/**
 * App列表Adapter
 * Created by Bruce on 2017/3/13.
 */
public class AppRecyclerAdapter extends RecyclerView.Adapter<AppRecyclerAdapter.AppInfoHolder> implements Filterable{

    private Context mContext;
    private List<AppInfo> mAppInfos;
    private LayoutInflater mLayoutInflater;
    private View mContainer;
    private ClipboardManager mClipboardManager;

    public AppRecyclerAdapter(Context context, List<AppInfo> appInfos) {
        mContext = context;
        mAppInfos = appInfos;
        mLayoutInflater = LayoutInflater.from(mContext);
        mClipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public AppInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContainer = mLayoutInflater.inflate(R.layout.item_app, parent, false);
        return new AppInfoHolder(mContainer);
    }

    @Override
    public void onBindViewHolder(final AppInfoHolder holder, final int position) {
        final AppInfo appInfo = mAppInfos.get(position);
        holder.appName.setText(appInfo.getAppName());
        holder.pckName.setText(appInfo.getPckName());
        holder.icon.setImageDrawable(appInfo.getIcon());

        holder.contentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("click -- > %d", position);
                holder.container.toggle();
            }
        });

        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy(appInfo.getPckName().toString());
            }
        });

        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppDetail(appInfo.getPckName().toString());
            }
        });

        holder.uninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uninstallApp(appInfo.getPckName().toString());
            }
        });

        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp(appInfo.getPckName().toString());
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApk(appInfo.getApkPath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAppInfos == null ? 0 : mAppInfos.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public void notifyAll(List<AppInfo> list) {
        mAppInfos.clear();
        mAppInfos.addAll(list);
        notifyDataSetChanged();
    }

    private void showToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 复制
     * @param text
     */
    private void copy(String text){
        ClipData data = ClipData.newPlainText(text, text);
        mClipboardManager.setPrimaryClip(data);
        showToast("复制成功");
    }

    /**
     * 打开应用信息界面
     * @param pckName
     */
    private void openAppDetail(String pckName) {
        Uri uri = Uri.parse("package:" + pckName);
        mContext.startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri));
    }

    /**
     * 打开应用
     * @param pckName
     */
    private void openApp(String pckName){
        try{
            Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(pckName);
            mContext.startActivity(intent);
        }catch (Exception e) {
            showToast("无法打开");
        }
    }

    /**
     * 卸载应用
     * @param pckName
     */
    private void uninstallApp(String pckName) {
        Uri uri = Uri.parse("package:" + pckName);
        mContext.startActivity(new Intent(Intent.ACTION_DELETE, uri));
    }

    /**
     * 分享APK
     * @param apkPath
     */
    private void shareApk(String apkPath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkPath)));
        intent.setType("application/vnd.android.package-archive");
        mContext.startActivity(Intent.createChooser(intent, "Share Apk"));
    }

    class AppInfoHolder extends RecyclerView.ViewHolder{

        ExpandableLayout container;
        RelativeLayout contentContainer;
        LinearLayout controlContainer;
        CardView cardView;
        ImageView icon;
        TextView appName;
        TextView pckName;
        TextView copy, detail, uninstall, open, share;

        AppInfoHolder(View itemView) {
            super(itemView);
            container = (ExpandableLayout) itemView.findViewById(R.id.el_item_app_container);
            contentContainer = (RelativeLayout) itemView.findViewById(R.id.rl_item_app_content_container);
            controlContainer = (LinearLayout) itemView.findViewById(R.id.ll_item_app_control_container);
            cardView = (CardView) itemView.findViewById(R.id.cv_item_app_card_view);
            icon = (ImageView) itemView.findViewById(R.id.iv_item_app_icon);
            appName = (TextView) itemView.findViewById(R.id.tv_item_app_name);
            pckName = (TextView) itemView.findViewById(R.id.tv_item_app_package_name);
            copy = (TextView) itemView.findViewById(R.id.tv_item_app_copy);
            detail = (TextView) itemView.findViewById(R.id.tv_item_app_detail);
            uninstall = (TextView) itemView.findViewById(R.id.tv_item_app_uninstall);
            open = (TextView) itemView.findViewById(R.id.tv_item_app_open);
            share = (TextView) itemView.findViewById(R.id.tv_item_app_share);
        }
    }
}
