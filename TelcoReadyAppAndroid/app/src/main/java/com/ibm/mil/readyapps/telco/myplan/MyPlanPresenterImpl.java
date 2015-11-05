/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.myplan;

import android.content.Context;

import com.ibm.mil.readyapps.telco.baseplan.BasePlan;
import com.ibm.mil.readyapps.telco.baseplan.BasePlanModelImpl;
import com.ibm.mil.readyapps.telco.cycles.Cycle;
import com.ibm.mil.readyapps.telco.cycles.CycleModel;
import com.ibm.mil.readyapps.telco.cycles.CycleModelImpl;
import com.ibm.mil.readyapps.telco.offers.Offer;
import com.ibm.mil.readyapps.telco.offers.OfferModel;
import com.ibm.mil.readyapps.telco.offers.OfferModelImpl;
import com.ibm.mil.readyapps.telco.utils.PlanConstants;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Presenter for My Plan tab responsible for setting up subscriptions to
 * desired streams and acting on those streams emitting new values.
 */
public class MyPlanPresenterImpl implements MyPlanPresenter {
    private final MyPlanView view;
    private final OfferModel offerModel;
    private final CycleModel cycleModel;

    /**
     * Constructor for initializing necessary class properties.
     *
     * @param view the view implemented by MyPlanFragment
     */
    public MyPlanPresenterImpl(MyPlanView view) {
        this.view = view;
        this.offerModel = new OfferModelImpl();
        this.cycleModel = new CycleModelImpl();
    }

    /**
     * Subscribe to talk cycle update stream and
     * call display cycle on the view each time
     * a new cycle is emitted from the stream.
     */
    @Override
    public void getTalkCycle() {
        cycleModel.getTalkCycleUpdates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Cycle>() {
                @Override
                public void call(Cycle cycle) {
                    view.displayTalkCycle(cycle);
                }
            });
    }

    /**
     * Subscribe to text cycle update stream and
     * call display cycle on the view each time
     * a new cycle is emitted from the stream.
     */
    @Override
    public void getTextCycle() {
        cycleModel.getTextCycleUpdates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Cycle>() {
                @Override
                public void call(Cycle cycle) {
                    view.displayTextCycle(cycle);
                }
            });
    }

    /**
     * Subscribe to data cycle update stream and
     * call display cycle on the view each time
     * a new cycle is emitted from the stream.
     */
    @Override
    public void getDataCycle() {
        cycleModel.getDataCycleUpdates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Cycle>() {
                @Override
                public void call(Cycle cycle) {
                    view.displayDataCycle(cycle);
                }
            });
    }

    /**
     * Subscribe to offers stream and update offers
     * in view as they are emitted from stream.
     *
     * @param ctx the context needed to get initial offers
     */
    @Override
    public void getOffers(Context ctx) {
        offerModel.getOffers(ctx)
            .subscribeOn(Schedulers.io())
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<Offer>>() {
                @Override
                public void call(List<Offer> offers) {
                    view.displayOffers(offers);
                }
            });
    }

    /**
     * Subscribe to accepted offer stream passed in
     * and accept an offer on the model as new
     * accepted offers emitted.
     *
     * @param observable the stream to subscribe to
     */
    @Override
    public void acceptOffers(Observable<Offer> observable) {
        observable
            .observeOn(Schedulers.io())
            .subscribe(new Action1<Offer>() {
                @Override
                public void call(Offer offer) {
                    offerModel.acceptOffer(offer);
                }
            });
    }

    /**
     * Subscribe to stream for undoing accepted offers and
     * perform that action on model as stream emits new vals.
     *
     * @param observable the stream to subscribe to
     */
    @Override
    public void undoAcceptOffer(Observable<Offer> observable) {
        observable
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Offer>() {
                @Override
                public void call(Offer offer) {
                    offerModel.undoAccept(offer);
                }
            });
    }

    /**
     * Subscribe to dismiss offer stream and update model
     * and view as the stream emits new offers
     *
     * @param observable the stream to subscribe to
     */
    @Override
    public void dismissOffer(Observable<Offer> observable) {
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Offer>() {
                @Override
                public void call(Offer offer) {
                    offerModel.dismissOffer(offer);
                    view.updateRemove(offer);
                }
            });
    }

    /**
     * Add an offer to the offer model and update the view.
     *
     * @param offer the offer to add
     */
    public void addOffer(Offer offer) {
        offerModel.addOffer(offer);
        view.displayOffer(offer);
    }

    /**
     * Subscribe to stream for cycles being updated and update
     * cycle model and view with new cycle as emitted.
     *
     * @param updateCycleStream the stream to subscribe to
     */
    @Override
    public void updateCycles(Observable<Cycle> updateCycleStream) {
        updateCycleStream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Cycle>() {
                @Override
                public void call(Cycle cycle) {
                    updateCycle(cycle);
                }
            });
    }

    /**
     * Subscribe to stream for base plan changes and update
     * view with new base plan as it is emitted.
     */
    @Override
    public void getBasePlanInfo() {
        new BasePlanModelImpl().getBasePlanStream()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<BasePlan>() {
                @Override public void call(BasePlan basePlan) {
                    view.displayBasePlanInfo(basePlan);
                }
            });
    }

    /**
     * update the cycle model and view with a new cycle.
     *
     * @param cycle the new cycle to update with
     */
    private void updateCycle(Cycle cycle) {
        switch (cycle.getType()) {
            case PlanConstants.DATA:
                cycleModel.updateDataCycle(cycle);
                view.updateDataCycle(cycle);
                break;
            case PlanConstants.TALK:
                cycleModel.updateTalkCycle(cycle);
                view.updateTalkCycle(cycle);
                break;
            case PlanConstants.TEXT:
                cycleModel.updateTextCycle(cycle);
                view.updateTextCycle(cycle);
                break;
        }
    }

}
