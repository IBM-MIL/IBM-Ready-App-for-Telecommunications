/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.usage;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.utils.PlanConstants;
import com.ibm.mil.readyapps.telco.views.TelcoUsageView;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Binder to append the user text/talk usage information
 * to the corresponding recyclerView ViewHolder
 */
public class IncomingOutgoingBinder extends DataBinder<IncomingOutgoingBinder.UsageViewHolder> {
    Usage currentUsage;
    Context context;
    Resources resources;


    /**
     * Constructor to initialize the cycle binder
     *
     * @param adapter the adapter that uses this binder to populate the recyclerView
     * @param context context from the main activity
     *               used to access application resources
     */
    public IncomingOutgoingBinder(DataBindAdapter adapter, Context context) {
        super(adapter);
        this.context = context;
        resources = context.getResources();
    }

    /**
     * creates a new ViewHolder using the provided xml layout
     *
     * @param viewGroup the parent ViewGroup that this ViewHolder will inflate
     * specifies the xml layout that the binder should use to create the view
     * @return the inflated view
     */
    @Override
    public UsageViewHolder newViewHolder(ViewGroup viewGroup) {
        View usageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.incoming_outgoing, viewGroup, false);
        return new UsageViewHolder(usageView);
    }

    /**
     * @param usageViewHolder the ViewHolder to use onBind
     *                   has all the associated views in this layout
     * @param position the position of this ViewHolder in the binder
     */
    @Override
    public void bindViewHolder(UsageViewHolder usageViewHolder, int position) {
        if (currentUsage.getType() == PlanConstants.TEXT) {
            usageViewHolder.incomingBar.setImageSource(ResourcesCompat.getDrawable(resources, R.drawable.intext, null));
            usageViewHolder.outgoingBar.setImageSource(ResourcesCompat.getDrawable(resources, R.drawable.outtext, null));
            usageViewHolder.incomingBar.setBottomRightText(currentUsage.getIncoming() + " " + PlanConstants.TEXT_UNIT);
            usageViewHolder.outgoingBar.setBottomRightText(currentUsage.getOutgoing() + " " + PlanConstants.TEXT_UNIT);
        } else {
            usageViewHolder.incomingBar.setBottomRightText(currentUsage.getIncoming() + " " + PlanConstants.TALK_UNIT);
            usageViewHolder.outgoingBar.setBottomRightText(currentUsage.getOutgoing() + " " + PlanConstants.TALK_UNIT);
        }
        usageViewHolder.incomingBar.setPercentUsed((currentUsage.getIncoming() * 100) / currentUsage.getTotal());
        usageViewHolder.outgoingBar.setPercentUsed((currentUsage.getOutgoing() * 100) / currentUsage.getTotal());
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    /**
     * @param usage to add to the offers list
     */
    public void add(Usage usage) {
        currentUsage = usage;
        notifyBinderDataSetChanged();
    }

    /**
     * Define the UsageViewHolder
     * inject views that need to be updated in the ViewHolder onBind
     */
    public class UsageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.incomingBar) TelcoUsageView incomingBar;
        @Bind(R.id.outgoingBar) TelcoUsageView outgoingBar;

        public UsageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
