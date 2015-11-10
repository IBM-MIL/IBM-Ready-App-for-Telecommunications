/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.mydata;

import android.content.Context;

import com.ibm.mil.readyapps.telco.cycles.Cycle;
import com.ibm.mil.readyapps.telco.offers.Offer;

import rx.Observable;
import rx.Subscription;

/**
 * Methods needed to be implemented by classes implementing DataPresenter.
 * Detailed method comments in DataPresenterImpl.java
 */
public interface DataPresenter {
    void getCycle();

    void getAppUsages(Context context);

    void getAppOffers(Context context);

    void getAcceptedOffers();

    void listenForUndoAccept();

    void undoRemoveOffer(Observable<Offer> observable);

    Subscription addAppUsage(Observable<Offer> appOfferObservable);

    void updateDataPlan(Observable<Cycle> cycleObservable);

    void removeOffer(Observable<Offer> removeOfferStream);

    void addAppOffer(Offer appOffer);
}
