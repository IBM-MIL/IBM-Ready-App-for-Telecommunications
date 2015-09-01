/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.mytext;

import android.content.Context;

import com.ibm.mil.readyapps.telco.cycles.Cycle;
import com.ibm.mil.readyapps.telco.cycles.CycleModel;
import com.ibm.mil.readyapps.telco.cycles.CycleModelImpl;
import com.ibm.mil.readyapps.telco.offers.Offer;
import com.ibm.mil.readyapps.telco.offers.OfferModel;
import com.ibm.mil.readyapps.telco.offers.OfferModelImpl;
import com.ibm.mil.readyapps.telco.usage.Usage;
import com.ibm.mil.readyapps.telco.usage.UsageModel;
import com.ibm.mil.readyapps.telco.usage.UsageModelImpl;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Presenter for Text tab responsible for setting up subscriptions to
 * desired streams and acting on those streams emitting new values.
 */
public class TextPresenterImpl implements TextPresenter {
    private final TextView view;
    private final CycleModel cycleModel;
    private final UsageModel usageModel;
    private final OfferModel offerModel;
    private final Context context;

    /**
     * Constructor for initializing necessary class properties.
     *
     * @param view the view implemented by TalkFragment.
     */
    public TextPresenterImpl(TextView view, Context context) {
        this.view = view;
        this.cycleModel = new CycleModelImpl();
        this.usageModel = new UsageModelImpl();
        this.offerModel = new OfferModelImpl();
        this.context = context;
    }

    /**
     * Subscribe to Text cycle update stream and
     * call display cycle on the view each time a
     * new cycle is emitted from the stream.
     */
    @Override
    public void getCycle() {
        cycleModel.getTextCycleUpdates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Cycle>() {
                    @Override
                    public void call(Cycle cycle) {
                        view.displayCycle(cycle);
                    }
                });
    }

    /**
     * Subscribe to text usage stream and update
     * the view as new items are emitted.
     */
    @Override
    public void getUsage() {
        usageModel.getTextUsage(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Usage>() {
                    @Override
                    public void call(Usage usage) {
                        view.displayUsage(usage);
                    }
                });
    }

    /**
     * Subscribe to accepted offers stream and display new
     * offers as they are emitted from stream.
     */
    @Override
    public void getAcceptedOffers() {
        offerModel.getAcceptedOffers()
                .subscribeOn(Schedulers.io())
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Offer>>() {
                    @Override
                    public void call(List<Offer> offers) {
                        view.displayAcceptedOffers(offers);
                    }
                });

        offerModel.getFutureAcceptedOffers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Offer>() {
                    @Override
                    public void call(Offer offer) {
                        view.displayAcceptedOffer(offer);
                    }
                });
    }

    /**
     * Subscribe to stream for undoing accepted offers
     *  and remove the offers from the view.
     */
    @Override
    public void listenForUndoAccept() {
        offerModel.getUndoOfferAcceptStream()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Offer>() {
                    @Override
                    public void call(Offer offer) {
                        view.removeAcceptedOffer(offer);
                    }
                });
    }

    /**
     * Subscribe to stream for updating cycle and update model
     * and view as new objects emitted.
     *
     * @param cycleObservable the stream to subscribe to
     */
    @Override
    public void updateTextPlan(Observable<Cycle> cycleObservable) {
        cycleObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Cycle>() {
                    @Override
                    public void call(Cycle cycle) {
                        cycleModel.updateTextCycle(cycle);
                        view.updateCycleView(cycle);
                    }
                });
    }

    /**
     * Subscribe to stream for removing offers and update
     * model and view as new objects emitted.
     *
     * @param removeOfferStream the stream to subscribe to
     */
    @Override
    public void removeOffer(Observable<Offer> removeOfferStream) {
        removeOfferStream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Offer>() {
                    @Override
                    public void call(Offer offer) {
                        offerModel.removeAcceptedOffer(offer);
                        view.removeAcceptedOffer(offer);
                    }
                });
    }

    /**
     * Subscribe to stream for undoing removal of an offer
     * and update view when it happens.
     *
     * @param observable the stream to subscribe to
     */
    @Override
    public void undoRemoveOffer(Observable<Offer> observable) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Offer>() {
                    @Override
                    public void call(Offer offer) {
                        view.displayAcceptedOffer(offer);
                    }
                });
    }
}