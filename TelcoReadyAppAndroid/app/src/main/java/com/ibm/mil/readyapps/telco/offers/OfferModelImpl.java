/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.offers;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.ibm.mil.readyapps.telco.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Implementation of methods in OfferModel interface
 */
public class OfferModelImpl implements OfferModel {
    private static List<Offer> offers;
    private static List<Offer> appOffers;
    private static List<Offer> acceptedOffers;

    private static final PublishSubject<Offer> futureAcceptedOffers = PublishSubject.create();
    private static final PublishSubject<Offer> appOfferStream = PublishSubject.create();
    private static final PublishSubject<Offer> undoOfferAcceptStream = PublishSubject.create();
    private static final PublishSubject<Offer> undoOfferRemoveStream = PublishSubject.create();

    @Override
    public Observable<Offer> getAppOffers(Context context) {
        return Observable.from(createAppOffers(context));
    }

    @Override
    public Observable<Offer> getAcceptedOffers() {
        return Observable.from(createAcceptedOffers());
    }

    @Override
    public Observable<Offer> getOffers(Context context) {
        return Observable.from(createUnacceptedOffers(context));
    }

    @Override public Observable<Offer> getFutureAcceptedOffers() {
        return futureAcceptedOffers;
    }

    @Override
    public Observable<Offer> getUndoOfferAcceptStream() {
        return undoOfferAcceptStream;
    }

    @Override
    public Observable<Offer> getUndoOfferRemoveStream() {
        return undoOfferRemoveStream;
    }

    @Override
    public Observable<Offer> getAppOfferStream() {
        return appOfferStream;
    }

    /**
     * Add offer to the model
     * @param offer to add
     */
    @Override
    public void addOffer(Offer offer) {
        offers.add(offer);
    }

    /**
     * Accept app offer
     * Remove from app offers list
     * Update corresponding app usage
     *
     * @param offer to accept
     */
    @Override
    public void acceptAppOffer(Offer offer) {
        int index = getIndex(offer, appOffers);
        if (index != -1) {
            appOffers.remove(index);
            appOfferStream.onNext(offer);
        }
        index = getIndex(offer, acceptedOffers);
        if(index == -1) {
            acceptedOffers.add(offer);
        }
    }

    /**
     * Find the index of a given offer in the given offer list
     *
     * @param offer to find the index
     * @param offerList to look for the offer
     * @return index of offer in the list
     */
    private int getIndex(Offer offer, List<Offer> offerList) {
        for(int i=0; i<offerList.size(); i++)
        {
            if((offer.getAppName()).equals(offerList.get(i).getAppName())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Accept offer
     * Remove from offers list
     * Update corresponding app usage
     *
     * @param offer to accept
     */
    @Override
    public void acceptOffer(Offer offer) {
        futureAcceptedOffers.onNext(offer);
        offers.remove(offer);
        acceptedOffers.add(offer);
    }

    /**
     * Undo offer removal
     * Add back to accepted offers list
     *
     * @param offer to undo removal
     */
    @Override
    public void undoRemove(Offer offer) {
        offers.remove(offer);
        acceptedOffers.add(offer);
        undoOfferRemoveStream.onNext(offer);
    }

    /**
     * Undo offer accept
     *
     * @param offer to undo accept
     */
    @Override
    public void undoAccept(Offer offer) {
        acceptedOffers.remove(offer);
        undoOfferAcceptStream.onNext(offer);
    }

    /**
     * Dismiss offer
     *
     * @param offer to dismiss
     */
    @Override
    public void dismissOffer(Offer offer) {
        offers.remove(offer);
    }

    /**
     * Add offer from recharge
     *
     * @param offer to add
     */
    @Override
    public void addCardFromManualRecharge(Offer offer) {
        acceptedOffers.add(offer);
        futureAcceptedOffers.onNext(offer);
    }

    /**
     * Remove accepted offer
     *
     * @param offer to remove
     */
    @Override
    public void removeAcceptedOffer(Offer offer) {
        acceptedOffers.remove(offer);
    }

    /**
     * Add app offer
     *
     * @param offer to add
     */
    @Override
    public void addAppOffer(Offer offer) {
        appOffers.add(offer);
    }

    /**
     * @return boolean indicating if offers list is empty
     */
    @Override
    public boolean noOffers() {
        return offers.isEmpty();
    }

    /**
     * @return boolean indicating if appOffers list is empty
     */
    @Override
    public boolean noAppOffers() {
        return appOffers.isEmpty();
    }

    /**
     * @return boolean indicating if acceptedOffers list is empty
     */
    @Override
    public boolean noAcceptedOffers(){
        return acceptedOffers.isEmpty();
    }

    /**
     * Get offers from the json file and save in cache
     *
     * @param ctx used in the JsonUtils
     * @return offers either from the json file or from cache
     */
    private List<Offer> createUnacceptedOffers(Context ctx) {
        if(offers != null) {
            return offers;
        }
        TypeToken<List<Offer>> token = new TypeToken<List<Offer>>() {
        };
        List<Offer> initialOffers = JsonUtils.parseJsonFile(ctx, "offers.json", token);

        for(Offer offer: initialOffers){
            int resId = ctx.getResources().getIdentifier(offer.getIcon(), "drawable", ctx.getPackageName());
            offer.setCardIcon(resId);
        }
        offers = initialOffers;
        return offers;
    }

    /**
     * Create an accepted offer list and save in cache
     *
     * @return app offers either new list or from cache
     */
    private List<Offer> createAcceptedOffers() {
        if (acceptedOffers != null) {
            return acceptedOffers;
        }

        acceptedOffers = new ArrayList<>();

        return acceptedOffers;
    }

    /**
     * Get app offers from the json file and save in cache
     *
     * @param context used in the JsonUtils
     * @return app offers either from the json file or from cache
     */
    private List<Offer> createAppOffers(Context context) {
        if (appOffers != null) {
            return appOffers;
        }

        TypeToken<List<Offer>> token = new TypeToken<List<Offer>>() {
        };
        List<Offer> initialOffers = JsonUtils.parseJsonFile(context, "app_offers.json", token);

        for(Offer offer: initialOffers){
            int resId = context.getResources().getIdentifier(offer.getIcon(), "drawable", context.getPackageName());
            offer.setCardIcon(resId);
        }

        appOffers = initialOffers;

        return appOffers;
    }
}
