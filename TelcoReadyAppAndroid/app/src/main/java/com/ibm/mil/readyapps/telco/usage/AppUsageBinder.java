/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.usage;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.utils.PlanConstants;
import com.ibm.mil.readyapps.telco.views.RobotoTextView;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Binder to append the user app usage information
 * to the corresponding recyclerView ViewHolder
 */
public class AppUsageBinder extends DataBinder<AppUsageBinder.AppUsageViewHolder> {

    Context context;
    Resources resources;
    private List<Usage> appUsages;

    /**
     * Constructor to initialize the cycle binder
     *
     * @param adapter the adapter that uses this binder to populate the recyclerView
     * @param context context from the main activity
     *               used to access application resources
     */
    public AppUsageBinder(DataBindAdapter adapter, Context context) {
        super(adapter);
        this.context = context;
        this.resources = context.getResources();
        appUsages = new ArrayList<>();
    }

    /**
     * creates a new ViewHolder using the provided xml layout
     *
     * @param viewGroup the parent ViewGroup that this ViewHolder will inflate
     * specifies the xml layout that the binder should use to create the view
     * @return the inflated view
     */
    @Override
    public AppUsageViewHolder newViewHolder(ViewGroup viewGroup) {
        View appUsageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_app_usage, viewGroup, false);
        return new AppUsageViewHolder(appUsageView);
    }

    /**
     * @param appUsageViewHolder the ViewHolder to use onBind
     *                   has all the associated views in this layout
     * @param position the position of this ViewHolder in the binder
     */
    @Override
    public void bindViewHolder(AppUsageViewHolder appUsageViewHolder, int position) {
        Usage dataUsage = appUsages.get(position);
        appUsageViewHolder.usageProgressBar.setVisibility(View.VISIBLE);
        appUsageViewHolder.usageBottomLeftText.setTextSize(12);
        appUsageViewHolder.usageBottomLeftText.setTextColor(resources.getColor(R.color.light_gray));
        appUsageViewHolder.usageBottomLeftText.setFont(context, resources.getString(R.string.roboto_regular));
        appUsageViewHolder.usageBottomLeftText.setText(dataUsage.getUsed() + " " + PlanConstants.DATA_UNIT);
        appUsageViewHolder.usageTopText.setText(dataUsage.getAppName());
        appUsageViewHolder.usageImage.setImageResource(dataUsage.getUsageImage());

        dataUsage.setSeekBarProgress((int) ((dataUsage.getUsed() * 100) / dataUsage.getLimit()));

        appUsageViewHolder.usageProgressBar.setProgress(dataUsage.getSeekBarProgress());
        appUsageViewHolder.usageBottomRightText.setText(dataUsage.getSeekBarProgress() + resources.getString(R.string.percent_used));

        if (dataUsage.isUnlimited()) {
            appUsageViewHolder.usageProgressBar.setVisibility(View.GONE);
            appUsageViewHolder.usageBottomLeftText.setTextSize(14);
            appUsageViewHolder.usageBottomLeftText.setTextColor(resources.getColor(R.color.dark_gray));
            appUsageViewHolder.usageBottomLeftText.setFont(context, resources.getString(R.string.roboto_medium));
            appUsageViewHolder.usageBottomRightText.setText(resources.getString(R.string.unlimited_offer));
        }
    }

    @Override
    public int getItemCount() {
        return appUsages.size();
    }

    /**
     * Add all usages for user to the binder dataset
     * @param usages to add
     */
    public void addAll(List<Usage> usages) {
        appUsages = usages;
        notifyBinderDataSetChanged();
    }

    /**
     * @param newUsage to add to the offers list
     */
    public void addNewUsage(Usage newUsage) {
        int index = findAppIndex(newUsage.getAppName());
        if(index != -1) {
            Usage tempUsage = appUsages.get(index);
            newUsage.setUsed(tempUsage.getUsed());
            appUsages.set(index, newUsage);
            notifyBinderDataSetChanged();
        }
    }

    /**
     * Given the app name, find the index of the app in the uasages list
     * If not there, return -1
     * @param appName to find
     * @return index if found, -1 if not
     */
    private int findAppIndex(String appName) {
        int index = -1;
        for(int i=0; i<appUsages.size(); i++){
            if (appUsages.get(i).getAppName().equals(appName)){
                index = i;
            }
        }
        return index;
    }

    /**
     * Define the AppUsageViewHolder
     * inject views that need to be updated in the ViewHolder onBind
     */
    public class AppUsageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.usage_image) ImageView usageImage;
        @Bind(R.id.usage_top_text) RobotoTextView usageTopText;
        @Bind(R.id.bottom_left_text) RobotoTextView usageBottomLeftText;
        @Bind(R.id.bottom_right_text) RobotoTextView usageBottomRightText;
        @Bind(R.id.progress_bar) ProgressBar usageProgressBar;

        public AppUsageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
