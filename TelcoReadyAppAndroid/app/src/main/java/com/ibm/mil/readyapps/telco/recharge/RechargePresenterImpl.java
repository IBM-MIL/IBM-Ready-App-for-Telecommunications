/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.recharge;

import android.content.Context;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.baseplan.BasePlanModel;
import com.ibm.mil.readyapps.telco.baseplan.BasePlanModelImpl;
import com.ibm.mil.readyapps.telco.cycles.CycleModel;
import com.ibm.mil.readyapps.telco.cycles.CycleModelImpl;
import com.ibm.mil.readyapps.telco.offers.Offer;
import com.ibm.mil.readyapps.telco.offers.OfferModel;
import com.ibm.mil.readyapps.telco.offers.OfferModelImpl;
import com.ibm.mil.readyapps.telco.utils.PlanConstants;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Presenter for Recharge screen. Handles updating the
 * recharge model, updating the recharge view, and
 * subscribing to recharge stream.
 */
public class RechargePresenterImpl implements RechargePresenter {
    private final RechargeView view;
    private final RechargeModel model;

    private Recharge recharge;
    private @PlanConstants.Type final int type;
    private final String title;

    public RechargePresenterImpl(RechargeView view, @PlanConstants.Type int type, String title) {
        this.view = view;
        this.model = new RechargeModelImpl();
        this.type = type;
        this.title = title;
    }

    /**
     * Increase the recharge cost and amount by calling
     * appropriate model methods.
     */
    @Override
    public void increaseAmount() {
        model.changeAmount(recharge.getCurrentAmount() + recharge.getAmountStep());
        model.changeCost(recharge.getCurrentCost() + recharge.getCostStep());
    }

    /**
     * Decrease the recharge cost and amount by calling
     * appropriate model methods.
     */
    @Override
    public void decreaseAmount() {
        if (recharge.getCurrentAmount() > recharge.getInitialAmount()) {
            model.changeAmount(recharge.getCurrentAmount() - recharge.getAmountStep());
            model.changeCost(recharge.getCurrentCost() - recharge.getCostStep());
        }
    }

    /**
     * Accept the recharge by updating the base plan, the offer model,
     * and the cycle model and closing the view.
     *
     * @param context the context needed for build recharge method
     *                to access strings resources
     */
    @Override
    public void accept(Context context) {
        BasePlanModel basePlanModel = new BasePlanModelImpl();
        OfferModel offerModel = new OfferModelImpl();
        CycleModel cycleModel = new CycleModelImpl();

        basePlanModel.updateAddonCost(recharge.getCurrentCost());
        offerModel.addCardFromManualRecharge(buildRechargeOffer(context));

        cycleModel.setLimit(type, recharge.getCurrentAmount());
        
        view.close();
    }

    /**
     * Build an offer object from a Recharge object. Useful after
     * user accepts a recharge which must then be converted to
     * an offer card to be shown.
     *
     * @param context the context needed to access string resources
     * @return the Offer object built from the recharge
     */
    private Offer buildRechargeOffer(Context context) {
        String prefix = context.getString(R.string.you_added);
        String suffix = context.getString(R.string.to_plan);
        String body = String.format(context.getResources().getConfiguration().locale ,"%s %d %s %s.", prefix, recharge.getCurrentAmount(),
                recharge.getUnits(), suffix);

        Offer offer = new Offer(recharge.getTitle(), body, recharge.getDrawableIcon(),
                recharge.getCurrentCost(), "recharge",
                false, false, false);
        offer.setType(type);
        offer.setAmountAddedToCycle(recharge.getCurrentAmount());

        return offer;
    }

    /**
     * Subscribe to Recharge stream and update this recharge reference
     * and update text views on view as new recharges emitted to stream.
     */
    @Override
    public void createRecharge() {
        model.getRecharge(type, title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Recharge>() {
                    @Override public void call(Recharge recharge) {
                        RechargePresenterImpl.this.recharge = recharge;
                    }
                })
                .subscribe(new Action1<Recharge>() {
                    @Override public void call(Recharge recharge) {
                        view.updateTextViews(recharge);
                    }
                });
    }

}
