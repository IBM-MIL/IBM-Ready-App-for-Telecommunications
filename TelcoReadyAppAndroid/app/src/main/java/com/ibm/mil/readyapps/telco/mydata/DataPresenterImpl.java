/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.mydata;

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

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Presenter for Data tab responsible for setting up subscriptions to
 * desired streams and acting on those streams emitting new values.
 */
public class DataPresenterImpl implements DataPresenter {
    private final DataView view;
    private final CycleModel cycleModel;
    private final UsageModel usageModel;
    private final OfferModel offerModel;

    /**
     * Constructor for initializing necessary class properties.
     *
     * @param view the view implemented by DataFragment.
     */
    public DataPresenterImpl(DataView view) {
        this.view = view;
        this.cycleModel = new CycleModelImpl();
        this.usageModel = new UsageModelImpl();
        this.offerModel = new OfferModelImpl();
    }

    /**
     * Subscribe to Data cycle update stream and
     * call display cycle on the view each time a
     * new cycle is emitted from the stream.
     */
    @Override
    public void getCycle() {
        cycleModel.getDataCycleUpdates()
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
     * Subscribe to app usages stream and display new
     * usages as they are emitted from stream.
     *
     * @param context the context needed to get initial usages
     */
    @Override
    public void getAppUsages(Context context) {
        usageModel.getUsages(context)
                .subscribeOn(Schedulers.io())
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Usage>>() {
                    @Override
                    public void call(List<Usage> usages) {
                        view.displayAppUsages(usages);
                    }
                });
    }

    /**
     * Subscribe to app offers stream and display new
     * offers as they are emitted from stream.
     *
     * @param context the context needed to get initial offers
     */
    @Override
    public void getAppOffers(Context context) {
        offerModel.getAppOffers(context)
                .subscribeOn(Schedulers.io())
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Offer>>() {
                    @Override
                    public void call(List<Offer> offers) {
                        view.displayAppOffers(offers);
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
                        if (offer.isAppOffer()) {
                            addAppOffer(offer);
                        } else {
                            addOffer(offer);
                        }
                    }
                });
    }

    /**
     * Update data cycle usage
     *
     * @param usage to update by
     */
    private void updateCycleUsage(float usage) {
        Cycle cycle = cycleModel.getDataCycle();
        float newUsed = cycle.getUsed() + usage;
        DecimalFormat df;
        if(Locale.getDefault().toString().equals("en_US")){
            df = new DecimalFormat("#.##");
        }
        else{
            df = new DecimalFormat("#,##");
        }
        newUsed = Float.valueOf(df.format(newUsed));
        cycle.setUsed(newUsed);
        cycleModel.updateDataCycle(cycle);
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
                        if (offer.isAppOffer()) {
                            updateUsage(offer);
                            updateAppOffer(offer);
                            if(offer.isUnlimited()) {
                                updateCycleUsage(offer.getUsage());
                            }
                        }
                    }
                });
    }

    /**
     * Subscribe to stream for adding app usage.
     *
     * @param appOfferObservable the stream to subscribe to
     */
    @Override
    public Subscription addAppUsage(Observable<Offer> appOfferObservable) {
        return appOfferObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Offer>() {
                    @Override
                    public void call(Offer offer) {
                        addUsage(offer);
                        addOffer(offer);
                        offer.setBody(offer.getBody());
                        if(offer.isUnlimited()){
                           updateCycleUsage(-offer.getUsage());
                        }
                        view.removeAppOffer(offer);
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
    public void updateDataPlan(Observable<Cycle> cycleObservable) {
        cycleObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Cycle>() {
                    @Override
                    public void call(Cycle cycle) {
                        cycleModel.updateDataCycle(cycle);
                        updateDataPlan(cycle);
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
                        if (offer.isAppOffer()) {
                            updateUsage(offer);
                            updateAppOffer(offer);
                            if (offer.isUnlimited()) {
                                updateCycleUsage(offer.getUsage());
                            }
                        }
                    }
                });
    }

    /**
     * Add an app offer to the model.
     *
     * @param appOffer the offer to add
     */
    @Override
    public void addAppOffer(Offer appOffer) {
        offerModel.acceptAppOffer(appOffer);
    }

    /**
     * Update the model and the view when a new offer should be
     * added to app offer section.
     *
     * @param offer the offer to add to app offer section
     */
    private void updateAppOffer(Offer offer) {
        offerModel.addAppOffer(offer);
        view.displayAppOffer(offer);
    }

    /**
     * Update app usage section model and view.
     *
     * @param offer the offer to update usage section with
     */
    private void updateUsage(Offer offer) {
        Usage usage = usageModel.setLimitedUsage(offer);
        view.displayNewUsage(usage);
    }

    /**
     * Subscribe to stream for undoing removal of an offer
     * and update model and view when it happens.
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
                        if(offer.isAppOffer()){
                            offerModel.acceptAppOffer(offer);
                        }
                        else{
                            view.displayCardOffer(offer);
                        }

                    }
                });
    }

    /**
     * Update view with an added offer.
     *
     * @param offer the offer to add
     */
    private void addOffer(Offer offer) {
        view.displayCardOffer(offer);
    }

    /**
     * Update model and view with a new app usage.
     *
     * @param offer the app usage offer to update with
     */
    private void addUsage(Offer offer) {
        Usage newUsage = new Usage();
        newUsage = newUsage.offerToUsage(offer);
        usageModel.setNewUsage(newUsage);
        view.displayNewUsage(newUsage);
    }

    /**
     * Update the view with new cycle.
     *
     * @param cycle the cycle to refresh view with
     */
    private void updateDataPlan(Cycle cycle) {
        view.updateCycleView(cycle);
    }
}
