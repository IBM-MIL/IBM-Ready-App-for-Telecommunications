/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.myplan;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;

import com.ibm.mil.readyapps.telco.baseplan.BasePlan;
import com.ibm.mil.readyapps.telco.baseplan.BasePlanBinder;
import com.ibm.mil.readyapps.telco.cycles.Cycle;
import com.ibm.mil.readyapps.telco.cycles.CycleBinder;
import com.ibm.mil.readyapps.telco.offers.Offer;
import com.ibm.mil.readyapps.telco.offers.OfferBinder;
import com.ibm.mil.readyapps.telco.offers.OfferBinderHeader;
import com.ibm.mil.readyapps.telco.utils.RecyclerDivider;
import com.yqritc.recyclerviewmultipleviewtypesadapter.ListBindAdapter;

import java.util.List;

/**
 * Adapter for MyPlan tab RecyclerView. Extends class from
 * RecyclerView-MultipleViewTypesAdapter GitHub library.
 */
class MyPlanRecyclerAdapter extends ListBindAdapter {
    private static final int TYPE_BASE_PLAN = 0;
    private static final int TYPE_OVERVIEW = 1;
    private static final int TYPE_DIVIDER = 2;
    private static final int TYPE_CARD = 3;
    private final CycleBinder cycleBinder;
    private final OfferBinder offerBinder;

    /**
     * Constructor for setting up the adapter
     *
     * @param layout    the coordinator layout needs to be passed down to binder
     *                  to support moving FAB button on click listeners from binder
     * @param context   needs to be passed down to binders to support loading resources
     *                  like color in binders
     */
    public MyPlanRecyclerAdapter(Context context, CoordinatorLayout layout) {
        PlanOverviewBinder planOverviewBinder = new PlanOverviewBinder(this, context);
        cycleBinder = new CycleBinder(this, layout, context);
        OfferBinderHeader offerBinderHeader = new OfferBinderHeader(this, context);
        BasePlanBinder basePlanBinder = new BasePlanBinder(this);
        offerBinder = new OfferBinder(this, layout, context);
        //add all the viewHolder binders to populate the recyclerView
        addAllBinder(basePlanBinder, planOverviewBinder, offerBinderHeader, offerBinder);
    }

    /**
     * Set base plan information in BasePlanBinder
     *
     * @param basePlan instance to update the basePlan with
     */
    public void setBasePlanInfo(BasePlan basePlan) {
        ((BasePlanBinder)getDataBinder(TYPE_BASE_PLAN)).add(basePlan);
    }

    /**
     * Set cycle information in PlanOverViewBinder for the data cycle
     *
     * @param dataCycle instance to set
     */
    public void setDataCycle(Cycle dataCycle) {
        ((PlanOverviewBinder)getDataBinder(TYPE_OVERVIEW)).addData(dataCycle);
    }

    /**
     * Set cycle information in PlanOverViewBinder for the talk cycle
     *
     * @param talkCycle instance to set
     */
    public void setTalkCycle(Cycle talkCycle) {
        ((PlanOverviewBinder)getDataBinder(TYPE_OVERVIEW)).addTalk(talkCycle);
    }

    /**
     * Set cycle information in PlanOverviewBinder for the text cycle
     *
     * @param textCycle instance to set
     */
    public void setTextCycle(Cycle textCycle) {
        ((PlanOverviewBinder)getDataBinder(TYPE_OVERVIEW)).addText(textCycle);
    }

    /**
     * Set the header for the offer binder
     * pass RecyclerDivider instance to the OfferBinderHeader to update the view
     *
     * @param offerHeader instance to set
     */
    public void setAcceptedOfferHeader(RecyclerDivider offerHeader) {
        ((OfferBinderHeader) getDataBinder(TYPE_DIVIDER)).add(offerHeader);
    }

    /**
     * Set all the available offers to the OfferBinder
     *
     * @param offers list of available offers
     */
    public void setOffers(List<Offer> offers) {
        ((OfferBinder)getDataBinder(TYPE_CARD)).addAll(offers);
    }

    /**
     * Set the offer to be removed from the OfferBinder
     *
     * @param offer to be removed
     */
    public void removeOffer(Offer offer){
        ((OfferBinder)getDataBinder(TYPE_CARD)).removeOffer(offer);
    }

    /**
     * Set a new offer in the binder
     *
     * @param offer new offer to set
     */
    public void addOffer(Offer offer) {
        ((OfferBinder)getDataBinder(TYPE_CARD)).addNewOffer(offer);
    }

    /**
     * @return the offerBinder instance
     */
    public OfferBinder getOfferBinder() {
        return offerBinder;
    }

    /**
     * @return the cycleBinder instance
     */
    public CycleBinder getCycleBinder() {
        return cycleBinder;
    }
}
