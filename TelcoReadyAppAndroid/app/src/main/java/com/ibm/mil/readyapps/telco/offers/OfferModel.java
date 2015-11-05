/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.offers;

import android.content.Context;

import rx.Observable;

/**
 * Methods for getting, setting and updating offer fields
 */
public interface OfferModel {

    Observable<Offer> getAppOffers(Context context);

    Observable<Offer> getAcceptedOffers();

    Observable<Offer> getOffers(Context context);

    Observable<Offer> getFutureAcceptedOffers();

    Observable<Offer> getUndoOfferAcceptStream();

    Observable<Offer> getUndoOfferRemoveStream();

    Observable<Offer> getAppOfferStream();

    void addOffer(Offer offer);

    void acceptAppOffer(Offer offer);

    void acceptOffer(Offer offer);

    void undoAccept(Offer offer);

    void undoRemove(Offer offer);

    void dismissOffer(Offer offer);

    void addCardFromManualRecharge(Offer offer);

    void removeAcceptedOffer(Offer offer);

    void addAppOffer(Offer offer);

    boolean noOffers();

    boolean noAppOffers();

    boolean noAcceptedOffers();


}
