/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.mydata;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;

import com.ibm.mil.readyapps.telco.cycles.Cycle;
import com.ibm.mil.readyapps.telco.cycles.CycleBinder;
import com.ibm.mil.readyapps.telco.offers.AcceptedOfferBinder;
import com.ibm.mil.readyapps.telco.offers.MyAppHeaderBinder;
import com.ibm.mil.readyapps.telco.offers.MyAppsBinder;
import com.ibm.mil.readyapps.telco.offers.Offer;
import com.ibm.mil.readyapps.telco.usage.AppUsageBinder;
import com.ibm.mil.readyapps.telco.usage.Usage;
import com.ibm.mil.readyapps.telco.usage.UsageBinderHeader;
import com.ibm.mil.readyapps.telco.utils.RecyclerDivider;
import com.ibm.mil.readyapps.telco.utils.RecyclerDividerBinder;
import com.yqritc.recyclerviewmultipleviewtypesadapter.ListBindAdapter;

import java.util.List;

/**
 * Adapter for Data tab RecyclerView. Extends class from
 * RecyclerView-MultipleViewTypesAdapter GitHub library.
 */
public class DataRecyclerAdapter extends ListBindAdapter {

    private static final int TYPE_CURRENT_CYCLE = 0;
    private static final int TYPE_DATA_HEADER = 1;
    private static final int TYPE_APP_USAGE = 2;
    private static final int TYPE_MY_APPS_HEADER = 3;
    private static final int TYPE_MY_APPS = 4;
    private static final int TYPE_OFFER_HEADER = 5;
    private static final int TYPE_CARDS = 6;
    private static CycleBinder cycleBinder;
    private final AcceptedOfferBinder offerBinder;
    private final MyAppsBinder myAppsBinder;
    private final AppUsageBinder appUsageBinder;

    /**
     * Constructor for setting up the adapter with necessary args passed in.
     *
     * @param layout    the coordinator layout needs to be passed down to binder
     *                  to support moving FAB button on click listeners from binder
     * @param context   needs to be passed down to binders to support loading resources
     *                  like color in binders
     * @param presenter needed to pass down to my apps binder to communicate with stream
     */

    public DataRecyclerAdapter(CoordinatorLayout layout, Context context, DataPresenter presenter) {
        cycleBinder = new CycleBinder(this, layout, context);
        UsageBinderHeader appUsageHeader = new UsageBinderHeader(this);
        appUsageBinder = new AppUsageBinder(this, context);
        MyAppHeaderBinder myAppsHeader = new MyAppHeaderBinder(this, context);
        myAppsBinder = new MyAppsBinder(this, context, presenter);
        RecyclerDividerBinder recyclerDividerBinder = new RecyclerDividerBinder(this);
        offerBinder = new AcceptedOfferBinder(this, layout, context);
        //add all the viewHolder binders to populate the recyclerView
        addAllBinder(cycleBinder, appUsageHeader, appUsageBinder,
                myAppsHeader, myAppsBinder, recyclerDividerBinder, offerBinder);
    }

    /**
     * Initialize a cycle object in the data binder.
     *
     * @param cycle the cycle object to init with
     */
    public void setCycle(Cycle cycle) {
        ((CycleBinder) getDataBinder(TYPE_CURRENT_CYCLE)).add(cycle);
    }

    /**
     * Initialize the binder with a data header/divider type.
     *
     * @param recyclerDivider the divider/header to initialize with
     */
    public void setDividerHeader(RecyclerDivider recyclerDivider) {
        ((UsageBinderHeader) getDataBinder(TYPE_DATA_HEADER)).add(recyclerDivider);
    }

    /**
     * Initialize app usages on binder.
     *
     * @param appUsages the app usages to initialize with
     */
    public void setAppUsage(List<Usage> appUsages) {
        ((AppUsageBinder) getDataBinder(TYPE_APP_USAGE)).addAll(appUsages);
    }

    /**
     * Initialize header for my app section in binder.
     *
     * @param myAppHeader the header to initialize with
     */
    public void setMyAppHeader(RecyclerDivider myAppHeader) {
        ((MyAppHeaderBinder) getDataBinder(TYPE_MY_APPS_HEADER)).add(myAppHeader);
    }

    /**
     * Initialize the my apps binder with app offers.
     *
     * @param offers the offers to initialize with
     */
    public void setMyApps(List<Offer> offers) {
        ((MyAppsBinder) getDataBinder(TYPE_MY_APPS)).addAll(offers);
    }

    /**
     * Initialize the accepted offer binder with card offers.
     *
     * @param offers the offers to initialize with
     */
    public void setCardOffers(List<Offer> offers) {
        ((AcceptedOfferBinder) getDataBinder(TYPE_CARDS)).addAll(offers);
    }

    /**
     * Update the app usage section with a usage.
     *
     * @param newUsage the usage to update with
     */
    public void setNewUsage(Usage newUsage) {
        ((AppUsageBinder) getDataBinder(TYPE_APP_USAGE)).addNewUsage(newUsage);
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
     * Set the header on the offer binder offer section.
     *
     * @param offerHeader the header to use
     */
    public void setAcceptedOfferHeader(RecyclerDivider offerHeader) {
        ((RecyclerDividerBinder) getDataBinder(TYPE_OFFER_HEADER)).add(offerHeader);
    }

    /**
     * Remove a card offer from the accepted offer binder.
     *
     * @param offer the offer to remove from accepted offers
     */
    public void removeCardOffer(Offer offer) {
        ((AcceptedOfferBinder) getDataBinder(TYPE_CARDS)).remove(offer);
    }

    /**
     * Add an app offer to my apps binder.
     *
     * @param offer the offer to add
     */
    public void setAppOffer(Offer offer) {
        ((MyAppsBinder)getDataBinder(TYPE_MY_APPS)).add(offer);
    }

    /**
     * Get the cycle binder.
     *
     * @return the cycle binder
     */
    public CycleBinder getCycleBinder() {
        return cycleBinder;
    }

    /**
     * Get the app usage binder.
     *
     * @return the app usage binder
     */
    public AppUsageBinder getAppUsageBinder() {
        return appUsageBinder;
    }

    /**
     * Get the my apps binder.
     *
     * @return the my apps binder
     */
    public MyAppsBinder getMyAppBinder() {
        return myAppsBinder;
    }

    /**
     * Get the offer binder.
     *
     * @return the offer binder
     */
    public AcceptedOfferBinder getOfferBinder() {
        return offerBinder;
    }

    /**
     * Remove an app offer from my apps binder.
     *
     * @param offer the offer to remove
     */
    public void removeAppOffer(Offer offer) {
        ((MyAppsBinder)getDataBinder(TYPE_MY_APPS)).removeAppOffer(offer);
    }
}