/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.cycles;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.baseplan.BasePlanModelImpl;
import com.ibm.mil.readyapps.telco.utils.PlanConstants;
import com.ibm.mil.readyapps.telco.views.RobotoTextView;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;

import java.text.DecimalFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Binder to append the user data/talk/text cycle information
 * to the corresponding recyclerView ViewHolder
 */
public class CycleBinder extends DataBinder<CycleBinder.CurrentCycleViewHolder> {

    private Cycle currentCycle;
    private final CoordinatorLayout snackbar_layout;
    private final Resources resources;
    private int DOLLARS_PER_STEP;
    private int STEP_AMOUNT;
    private int MAX_AMOUNT;
    private final BasePlanModelImpl basePlanModel;
    private final CycleModelImpl cycleModel = new CycleModelImpl();

    /**
     * Constructor to initialize the cycle binder
     *
     * @param adapter the adapter that uses this binder to populate the recyclerView
     * @param layout coordinator layout from the main activity
     *               used for inflating the snackBar
     * @param context context from the main activity
     *               used to access application resources
     */
    public CycleBinder(DataBindAdapter adapter, CoordinatorLayout layout, Context context) {
        super(adapter);
        this.snackbar_layout = layout;
        resources = context.getResources();
        this.basePlanModel = new BasePlanModelImpl();
    }

    /**
     * creates a new ViewHolder using the provided xml layout
     *
     * @param viewGroup the parent ViewGroup that this ViewHolder will inflate
     * specifies the xml layout that the binder should use to create the view
     * @return the inflated view
     */
    @Override
    public CurrentCycleViewHolder newViewHolder(ViewGroup viewGroup) {
        View currentCycleView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_current_cycle, viewGroup, false);
        return new CurrentCycleViewHolder(currentCycleView);
    }

    /**
     * @param viewHolder the ViewHolder to use onBind
     *                   has all the associated views in this layout
     * @param position the position of this ViewHolder in the binder
     */
    @Override
    public void bindViewHolder(CurrentCycleViewHolder viewHolder, int position) {
        float usedPercent = ((currentCycle.getUsed() * 100) / currentCycle.getLimit());
        DecimalFormat df ;
        if(Locale.getDefault().toString().equals("en_US")){
            df = new DecimalFormat("#.##");
        }
        else{
            df = new DecimalFormat("#,##");
        }
        usedPercent = (float)Float.valueOf(df.format(usedPercent));
        viewHolder.usageImage.setImageResource(currentCycle.getCycleImage());
        viewHolder.usageBottomLeftUsage.setText(currentCycle.getUsed() + "/" + currentCycle.getLimit() + " " + currentCycle.getUnit());
        viewHolder.usageBottomRightText.setText(usedPercent + resources.getString(R.string.percent_used));
        viewHolder.usageProgressBar.setProgress((int)usedPercent);
        viewHolder.monthlyDataUsage.setText(currentCycle.getLimit() + "");
        viewHolder.unit.setText(currentCycle.getUnit());
    }

    /**
     * Get the item count for the CycleBinder
     *
     * @return the number of items/layouts in the ViewHolder
     */
    @Override
    public int getItemCount() {
        return 1;
    }

     /**
     * Update the current cycle and set constants for this cycle instance
     *
     * @param cycle the updated cycle instance (data/talk/text)
     */
    public void add(Cycle cycle) {
        currentCycle = cycle;
        DOLLARS_PER_STEP = getDollarsPerStep();
        STEP_AMOUNT = getStepAmount();
        MAX_AMOUNT = getMaxAmount();
        notifyBinderDataSetChanged();
    }

    /**
     * Get the step amount from PlanConstants based on the current cycle type
     *
     * @return the step amount for the current cycle
     */
    private int getStepAmount() {
        switch (currentCycle.getType()) {
            case PlanConstants.DATA:
                return PlanConstants.DATA_STEP_AMOUNT;
            case PlanConstants.TALK:
                return PlanConstants.TALK_STEP_AMOUNT;
            case PlanConstants.TEXT:
                return PlanConstants.TEXT_STEP_AMOUNT;
        }
        return 0;
    }

    /**
     * Get the monetary unit the from PlanConstants based on the current cycle type
     * Note: this is not necessarily in dollars, the amount will be in whichever
     * monetary unit the user is using
     * @return the step amount for the current cycle
     */
    private int getDollarsPerStep() {
        switch (currentCycle.getType()) {
            case PlanConstants.DATA:
                return PlanConstants.DATA_DOLLARS_PER_STEP;
            case PlanConstants.TALK:
                return PlanConstants.TALK_DOLLARS_PER_STEP;
            case PlanConstants.TEXT:
                return PlanConstants.TEXT_DOLLARS_PER_STEP;
        }
        return 0;
    }

    /**
     * Get the maximum allowed limit amount from PlanConstants
     *
     * @return the maximum limit amount for the current cycle
     */
    private int getMaxAmount() {
        switch (currentCycle.getType()) {
            case PlanConstants.DATA:
                return PlanConstants.DATA_MAX_AMOUNT;
            case PlanConstants.TALK:
                return PlanConstants.TALK_MAX_AMOUNT;
            case PlanConstants.TEXT:
                return PlanConstants.TEXT_MAX_AMOUNT;
        }
        return 0;
    }

    /**
     * Update the navigation bar to
     * reflect the base plan changes
     *
     * @param addedCost the amount needed to be added to the baseCost
     */
    private void updateBasePlan(int addedCost) {
        basePlanModel.updateBaseCost(addedCost);
    }

    /**
     * Define the CurrentCycleViewHolder
     * bind views that need to be updated in the ViewHolder onBind
     */
    public class CurrentCycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.usage_image) ImageView usageImage;
        @Bind(R.id.bottom_left_text) RobotoTextView usageBottomLeftUsage;
        @Bind(R.id.bottom_right_text) RobotoTextView usageBottomRightText;
        @Bind(R.id.progress_bar) ProgressBar usageProgressBar;
        @Bind(R.id.usage_gb) RobotoTextView monthlyDataUsage;
        @Bind(R.id.up_arrow) ImageView upArrow;
        @Bind(R.id.down_arrow) ImageView downArrow;
        @Bind(R.id.confirm) RobotoTextView confirmUpdate;
        @Bind(R.id.usage_unit) RobotoTextView unit;
        private int prevLimit = 0;

        public CurrentCycleViewHolder(View currentCycleView) {
            super(currentCycleView);
            ButterKnife.bind(this, currentCycleView);
            upArrow.setOnClickListener(this);
            downArrow.setOnClickListener(this);
            confirmUpdate.setOnClickListener(this);
        }

        /**
         * On click listener for the action buttons in the current cycle (up/down arrow, accept)
         *
         * @param view the view that was clicked
         */
        @Override
        public void onClick(View view) {
            int limit = Integer.parseInt(monthlyDataUsage.getText().toString());
            prevLimit = currentCycle.getLimit();

            if (view.equals(upArrow) && limit < MAX_AMOUNT) {
                limit = limit + STEP_AMOUNT;
                //change accept button color based on the selected amount
                if (limit == prevLimit)
                    confirmUpdate.setTextColor(resources.getColor(R.color.light_gray));
                else
                    confirmUpdate.setTextColor(resources.getColor(R.color.light_indigo));
            } else if (view.equals(downArrow) && limit > currentCycle.getUsed() && limit > PlanConstants.MIN_UNIT) {
                limit = limit - STEP_AMOUNT;
                //change accept button color based on the selected amount
                if (limit == prevLimit)
                    confirmUpdate.setTextColor(resources.getColor(R.color.light_gray));
                else
                    confirmUpdate.setTextColor(resources.getColor(R.color.light_indigo));
            } else if (view.equals(confirmUpdate) && limit != prevLimit) {
                updateLimit(limit);
                //also update the monthly cost in the navigation bar
                int addedCost = (limit - prevLimit) / STEP_AMOUNT * DOLLARS_PER_STEP;
                updateBasePlan(addedCost);
                createSnackBar(addedCost);
            }
            monthlyDataUsage.setText(limit + "");
        }

        /**
         * Update the limit of the specific cycle type
         * update the corresponding views
         *
         * @param limit the new limit amount to update to
         */
        private void updateLimit(int limit) {
            currentCycle.setLimit(limit);
            updatePlanCycles();
            confirmUpdate.setTextColor(resources.getColor(R.color.light_gray));
        }

        /**
         * create a SnackBar after changing base plan
         * the user gets ability to undo the plan change
         *
         * @param addedCost the amount added/removed after plan change
         *                  used to undo the plan change
         */
        private void createSnackBar(final int addedCost) {
            View.OnClickListener undoAction;
            /**
             * on click listener for undo of base plan change
             */
            undoAction = new View.OnClickListener() {
                //click listener for the SnackBar undo
                @Override
                public void onClick(View view) {
                    updateLimit(prevLimit);
                    basePlanModel.updateBaseCost(-addedCost);
                }
            };
            //build the SnackBar and display it
            Snackbar.make(snackbar_layout, resources.getString(R.string.baseplan_snackbar), Snackbar.LENGTH_LONG)
                    .setAction(resources.getString(R.string.undo), undoAction)
                    .setActionTextColor(resources.getColor(R.color.light_indigo))
                    .show();
        }
    }

    /**
     * pass the current cycle to the corresponding data/talk/text presenter
     */
    private void updatePlanCycles() {
        switch (currentCycle.getType()) {
            case PlanConstants.DATA:
                cycleModel.updateDataCycle(currentCycle);
                break;
            case PlanConstants.TALK:
                cycleModel.updateTalkCycle(currentCycle);
                break;
            case PlanConstants.TEXT:
                cycleModel.updateTextCycle(currentCycle);
                break;
        }
    }

}
