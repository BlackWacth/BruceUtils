package com.bruce.ui.model.app.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bruce.R;
import com.bruce.entity.AppInfo;

import java.util.List;

/**
 * App列表Adapter
 * Created by Bruce on 2017/3/13.
 */
public class AppRecyclerAdapter extends RecyclerView.Adapter<AppRecyclerAdapter.AppInfoHolder> implements Filterable{

    private Context mContext;
    private List<AppInfo> mAppInfos;
    private LayoutInflater mLayoutInflater;

    public AppRecyclerAdapter(Context context, List<AppInfo> appInfos) {
        mContext = context;
        mAppInfos = appInfos;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public AppInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppInfoHolder(mLayoutInflater.inflate(R.layout.item_app, parent, false));
    }

    @Override
    public void onBindViewHolder(AppInfoHolder holder, int position) {
        final AppInfo appInfo = mAppInfos.get(position);
        holder.appName.setText(appInfo.getAppName());
        holder.pckName.setText(appInfo.getPckName());
        holder.icon.setImageDrawable(appInfo.getIcon());
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

    class AppInfoHolder extends RecyclerView.ViewHolder{

        CardView container;
        ImageView icon;
        TextView appName;
        TextView pckName;

        public AppInfoHolder(View itemView) {
            super(itemView);
            container = (CardView) itemView.findViewById(R.id.cv_item_app_card_view);
            icon = (ImageView) itemView.findViewById(R.id.iv_item_app_icon);
            appName = (TextView) itemView.findViewById(R.id.tv_item_app_name);
            pckName = (TextView) itemView.findViewById(R.id.tv_item_app_package_name);
        }
    }

}
