/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.mytalk;

import com.ibm.mil.readyapps.telco.cycles.Cycle;
import com.ibm.mil.readyapps.telco.offers.Offer;

import rx.Observable;

/**
 * Methods needed to be implemented by classes implementing TalkPresenter.
 * Detailed method comments in TalkPresenterImpl.java
 */
interface TalkPresenter {

    void getCycle();

    void getUsage();

    void getAcceptedOffers();

    void listenForUndoAccept();

    void updateTalkPlan(Observable<Cycle> cycleObservable);

    void removeOffer(Observable<Offer> removeOfferStream);

    void undoRemoveOffer(Observable<Offer> undoRemoveOfferStream);

}
