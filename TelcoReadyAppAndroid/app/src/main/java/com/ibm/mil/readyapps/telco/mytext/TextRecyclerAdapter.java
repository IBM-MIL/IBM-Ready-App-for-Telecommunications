/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.mytext;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;

import com.ibm.mil.readyapps.telco.cycles.Cycle;
import com.ibm.mil.readyapps.telco.cycles.CycleBinder;
import com.ibm.mil.readyapps.telco.offers.Offer;
import com.ibm.mil.readyapps.telco.offers.AcceptedOfferBinder;
import com.ibm.mil.readyapps.telco.usage.IncomingOutgoingBinder;
import com.ibm.mil.readyapps.telco.usage.Usage;
import com.ibm.mil.readyapps.telco.utils.RecyclerDivider;
import com.ibm.mil.readyapps.telco.utils.RecyclerDividerBinder;
import com.yqritc.recyclerviewmultipleviewtypesadapter.ListBindAdapter;

import java.util.List;

/**
 * Adapter for Text tab RecyclerView. Extends class from
 * RecyclerView-MultipleViewTypesAdapter GitHub library.
 */
class TextRecyclerAdapter extends ListBindAdapter {
    private static final int TYPE_CURRENT_CYCLE = 0;
    private static final int TYPE_USAGE = 1;
    private static final int TYPE_DATA_HEADER = 2;
    private static final int TYPE_CARDS = 3;

    private static CycleBinder cycleBinder;
    private final AcceptedOfferBinder offerBinder;

    /**
     * Constructor for setting up the Talk adapter
     *
     * @param layout    the coordinator layout needs to be passed down to binder
     *                  to support moving FAB button on click listeners from binder
     * @param context   needs to be passed down to binders to support loading resources
     *                  like strings in binders
     */
    public TextRecyclerAdapter(CoordinatorLayout layout, Context context) {
        cycleBinder = new CycleBinder(this, layout, context);
        IncomingOutgoingBinder dataUsageBinder = new IncomingOutgoingBinder(this, context);
        RecyclerDividerBinder recyclerDividerBinder = new RecyclerDividerBinder(this);
        offerBinder = new AcceptedOfferBinder(this, layout, context);
        addAllBinder(cycleBinder, dataUsageBinder, recyclerDividerBinder, offerBinder);
    }

    /**
     * Set cycle information in CycleBinder for the text cycle
     *
     * @param cycle instance to set
     */
    public void setCycle(Cycle cycle) {
        ((CycleBinder) getDataBinder(TYPE_CURRENT_CYCLE)).add(cycle);
    }

    /**
     * Set incoming/outgoing usage information in IncomingOutgoingBinder
     *
     * @param usage instance to set
     */
    public void setIncomingOutgoing(Usage usage) {
        ((IncomingOutgoingBinder) getDataBinder(TYPE_USAGE)).add(usage);
    }

    /**
     * Set the header for the binder
     * pass RecyclerDivider instance to the RecyclerDividerBinder to update the view
     *
     * @param header instance to set
     */
    public void setDividerHeader(RecyclerDivider header) {
        ((RecyclerDividerBinder) getDataBinder(TYPE_DATA_HEADER)).add(header);
    }

    /**
     * Set all the available text offers to the AcceptedOfferBinder
     *
     * @param offers list of available offers
     */
    public void setCardOffers(List<Offer> offers) {
        ((AcceptedOfferBinder) getDataBinder(TYPE_CARDS)).addAll(offers);
    }

    /**
     * Set the offer to be removed from the AcceptedOfferBinder
     *
     * @param offer to be removed
     */
    public void removeCardOffer(Offer offer) {
        ((AcceptedOfferBinder) getDataBinder(TYPE_CARDS)).remove(offer);
    }

    /**
     * Add a new offer card in the binder.
     *
     * @param offer the offer to add
     */
    public void setCardOffer(Offer offer) {
        ((AcceptedOfferBinder) getDataBinder(TYPE_CARDS)).addNewCard(offer);
    }

    /**
     * @return the AcceptedOfferBinder instance
     */
    public AcceptedOfferBinder getOfferBinder() {
        return offerBinder;
    }

    /**
     * @return the cycleBinder instance
     */
    public CycleBinder getCycleBinder() {
        return cycleBinder;
    }

}
