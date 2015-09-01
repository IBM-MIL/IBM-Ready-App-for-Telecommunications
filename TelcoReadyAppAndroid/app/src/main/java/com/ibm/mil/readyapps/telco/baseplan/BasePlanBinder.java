/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.baseplan;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.utils.Currency;
import com.ibm.mil.readyapps.telco.views.RobotoTextView;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Binder to append the user base plan information
 * to the corresponding recyclerView ViewHolder
 */
public class BasePlanBinder extends DataBinder<BasePlanBinder.BasePlanViewHolder> {

    private BasePlan basePlan;

    /**
     * Constructor to initialize the binder
     *
     * @param dataBindAdapter the adapter that uses this binder to populate the recyclerView
     */
    public BasePlanBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    /**
     * Create a new ViewHolder using the provided xml layout
     *
     * @param viewGroup the parent ViewGroup that this ViewHolder will inflate
     * specifies the xml layout that the binder should use to create the view
     * @return the inflated view
     */
    @Override
    public BasePlanViewHolder newViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.base_plan_info, viewGroup, false);
        return new BasePlanViewHolder(view);
    }

    /**
     * Get the base and addon cost of the basePlan and
     * sets it to the corresponding TextViews in the ViewHolder
     *
     * @param basePlanViewHolder the ViewHolder to use onBind
     * @param position the position of this ViewHolder in the binder
     */
    @Override
    public void bindViewHolder(BasePlanViewHolder basePlanViewHolder, int position) {
        basePlanViewHolder.baseCost.setText(Currency.localize(basePlan.getBaseCost(), false));
        basePlanViewHolder.addonCost.setText(Currency.localize(basePlan.getAddonCost(), false));
    }

    /**
     * Get the item count for the BasePlanBinder
     *
     * @return the number of items/layouts in the ViewHolder
     */
    @Override
    public int getItemCount() {
        return 1;
    }

    /**
     * Set the base plan value and updates binder dataSet
     *
     * @param basePlan the new value of the base plan to be set
     */
    public void add(BasePlan basePlan) {
        this.basePlan = basePlan;
        notifyBinderDataSetChanged();
    }

    /**
     * Define the BasePlanViewHolder
     * bind views that need to be updated ViewHolder onBind
     */
    public class BasePlanViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.base_cost)RobotoTextView baseCost;
        @Bind(R.id.addon_cost)RobotoTextView addonCost;

        public BasePlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
