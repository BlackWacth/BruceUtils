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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bruce.R;
import com.bruce.entity.AppInfo;
import com.bruce.expandablelayoutlib.ExpandableLayout;
import com.orhanobut.logger.Logger;

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

        ExpandableLayout container;
        RelativeLayout contentContainer;
        LinearLayout controlContainer;
        CardView cardView;
        ImageView icon;
        TextView appName;
        TextView pckName;

        AppInfoHolder(View itemView) {
            super(itemView);
            container = (ExpandableLayout) itemView.findViewById(R.id.el_item_app_container);
            contentContainer = (RelativeLayout) itemView.findViewById(R.id.rl_item_app_content_container);
            controlContainer = (LinearLayout) itemView.findViewById(R.id.ll_item_app_control_container);
            cardView = (CardView) itemView.findViewById(R.id.cv_item_app_card_view);
            icon = (ImageView) itemView.findViewById(R.id.iv_item_app_icon);
            appName = (TextView) itemView.findViewById(R.id.tv_item_app_name);
            pckName = (TextView) itemView.findViewById(R.id.tv_item_app_package_name);
        }
    }

}
