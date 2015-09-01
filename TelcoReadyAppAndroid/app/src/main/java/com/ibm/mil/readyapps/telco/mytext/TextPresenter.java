/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.mytext;

import com.ibm.mil.readyapps.telco.cycles.Cycle;
import com.ibm.mil.readyapps.telco.offers.Offer;

import rx.Observable;

/**
 * Methods needed to be implemented by classes implementing TextPresenter.
 * Detailed method comments in TextPresenterImpl.java
 */
interface TextPresenter {

    void getCycle();

    void getUsage();

    void getAcceptedOffers();

    void listenForUndoAccept();

    void updateTextPlan(Observable<Cycle> cycleObservable);

    void removeOffer(Observable<Offer> removeOfferStream);

    void undoRemoveOffer(Observable<Offer> undoRemoveOfferStream);

}
