/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.myplan;

import android.content.Context;

import com.ibm.mil.readyapps.telco.cycles.Cycle;
import com.ibm.mil.readyapps.telco.offers.Offer;

import rx.Observable;

/**
 * Methods needed to be implemented by classes implementing MyPlanPresenter.
 * Detailed method comments in MyPlanPresenterImpl.java
 */
public interface MyPlanPresenter {
    void getTalkCycle();

    void getTextCycle();

    void getDataCycle();

    void getOffers(Context ctx);

    void acceptOffers(Observable<Offer> observable);

    void dismissOffer(Observable<Offer> observable);

    void undoAcceptOffer(Observable<Offer> observable);

    void addOffer(Offer offer);

    void updateCycles(Observable<Cycle> updateCycleStream);

    void getBasePlanInfo();
}
