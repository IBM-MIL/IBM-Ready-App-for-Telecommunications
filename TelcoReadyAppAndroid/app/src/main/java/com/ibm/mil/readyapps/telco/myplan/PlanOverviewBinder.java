/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.myplan;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.cycles.Cycle;
import com.ibm.mil.readyapps.telco.views.RobotoTextView;
import com.ibm.mil.readyapps.telco.views.TelcoUsageView;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlanOverviewBinder extends DataBinder<PlanOverviewBinder.CurrentPlanViewHolder> {
    private static Cycle dataCycle;
    private static Cycle talkCycle;
    private static Cycle textCycle;
    private final Resources resources;

    public PlanOverviewBinder(DataBindAdapter dataBindAdapter, Context context) {
        super(dataBindAdapter);
        resources = context.getResources();
    }

    @Override
    public CurrentPlanViewHolder newViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_plan_header, viewGroup, false);
        return new CurrentPlanViewHolder(view);
    }

    @Override
    public void bindViewHolder(CurrentPlanViewHolder currentPlanViewHolder, int i) {
        //setup data cycle info
        currentPlanViewHolder.dataUsageView.setPercentUsed((int) getUsedPercentage(dataCycle));
        currentPlanViewHolder.dataUsageView.setBottomLeftText(setUsedVsLimit(dataCycle));
        currentPlanViewHolder.dataUsageView.setBottomRightText(getUsedPercentage(dataCycle) + resources.getString(R.string.percent_used));
        //setup talk cycle info
        currentPlanViewHolder.talkUsageView.setPercentUsed((int) getUsedPercentage(talkCycle));
        currentPlanViewHolder.talkUsageView.setBottomLeftText(setUsedVsLimit(talkCycle));
        currentPlanViewHolder.talkUsageView.setBottomRightText(getUsedPercentage(talkCycle) + resources.getString(R.string.percent_used));
        //setup text cycle info
        currentPlanViewHolder.textUsageView.setPercentUsed((int) getUsedPercentage(textCycle));
        currentPlanViewHolder.textUsageView.setBottomLeftText(setUsedVsLimit(textCycle));
        currentPlanViewHolder.textUsageView.setBottomRightText(getUsedPercentage(textCycle) + resources.getString(R.string.percent_used));


        System.out.print(currentPlanViewHolder.myPlanView.getChildCount());
        RobotoTextView monthCycleTextView = ButterKnife.findById(currentPlanViewHolder.myPlanView, R.id.dateText);
        Locale currentLocale = Locale.getDefault();
        String myCalender = getMonthCycle(currentLocale);
        monthCycleTextView.setText(myCalender);
    }

    private String setUsedVsLimit(Cycle cycle) {
        return cycle.getUsed() + "/" + cycle.getLimit() + " " + cycle.getUnit();
    }

    /**
     * Get used percentage for this cycle
     *
     * @param currentCycle cycle to get used percentage for
     * @return used percentage amount
     */
    private float getUsedPercentage(Cycle currentCycle){
        float usedPercent = ((currentCycle.getUsed() * 100) / currentCycle.getLimit());
        DecimalFormat df;
        if(Locale.getDefault().toString().equals("en_US")){
            df = new DecimalFormat("#.##");
        }
        else{
            df = new DecimalFormat("#,##");
        }
        usedPercent = Float.valueOf(df.format(usedPercent));
        return usedPercent;
    }

    /**
     * Get the month cycle for the plan page
     *
     * @param currentLocale locale of the phone
     * @return currentDate the current date
     */
    private String getMonthCycle(Locale currentLocale) {
        Calendar cal = Calendar.getInstance(currentLocale);

        //get the startingDate - 3 weeks ago
        Calendar startingDate = (Calendar) cal.clone();
        startingDate.add(Calendar.DAY_OF_YEAR, -21);
        SimpleDateFormat date_format = new SimpleDateFormat("MMM d");
        String beginningDate = date_format.format(startingDate.getTime());

        //get out the ending date - 7 days in the future
        Calendar endingDate = (Calendar) cal.clone();
        endingDate.add(Calendar.DAY_OF_YEAR, 7);
        String endDate = date_format.format(endingDate.getTime());
        System.out.println(endDate);

        String monthCycle = beginningDate + " - " + endDate;

        return monthCycle;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    /**
     * @param dataCycle to update plan overview
     */
    public void addData(Cycle dataCycle) {
        PlanOverviewBinder.dataCycle = dataCycle;
        notifyBinderDataSetChanged();
    }

    /**
     * @param talkCycle to update plan overview
     */
    public void addTalk(Cycle talkCycle) {
        PlanOverviewBinder.talkCycle = talkCycle;
        notifyBinderDataSetChanged();
    }

    /**
     * @param textCycle to update plan overview
     */
    public void addText(Cycle textCycle) {
        PlanOverviewBinder.textCycle = textCycle;
        notifyBinderDataSetChanged();
    }

    public class CurrentPlanViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.dataUsageView)TelcoUsageView dataUsageView;
        @Bind(R.id.talkUsageView)TelcoUsageView talkUsageView;
        @Bind(R.id.textUsageView)TelcoUsageView textUsageView;
        @Bind(R.id.myPlanView)LinearLayout myPlanView;

        public CurrentPlanViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
