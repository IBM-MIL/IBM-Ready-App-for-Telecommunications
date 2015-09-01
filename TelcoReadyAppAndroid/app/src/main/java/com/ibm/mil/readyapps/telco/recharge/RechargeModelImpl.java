/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.recharge;

import com.ibm.mil.readyapps.telco.utils.PlanConstants;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Manages stream for Recharge POJO and makes updates to
 * POJO on behalf of callers. This class should be the only
 * class making modifications to the Recharge POJO. All
 * outside callers should go through this class to make
 * sure streams are emitted as Recharge POJO is updated.
 */
public class RechargeModelImpl implements RechargeModel {
    private static final PublishSubject<Recharge> stream = PublishSubject.create();
    private static Recharge recharge;

    /**
     * Get the recharge stream for being notified of changes to Recharge POJO.
     *
     * @param type the type of recharge stream to create and listen to
     * @param title the title wanted with the recharge
     * @return the stream to listen to for updates
     */
    @Override public Observable<Recharge> getRecharge(@PlanConstants.Type int type, String title) {
        recharge = RechargeFactory.createRecharge(type, title);
        return Observable.merge(Observable.just(recharge), stream);
    }

    /**
     * Change the amount for the recharge and emit new object on stream.
     *
     * @param amount the new amount to be associated with recharge
     */
    @Override public void changeAmount(int amount) {
        recharge.setCurrentAmount(amount);
        stream.onNext(recharge);
    }

    /**
     * Change the cost for the recharge and emit new object on stream.
     *
     * @param cost the new cost to set for the recharge
     */
    @Override public void changeCost(int cost) {
        recharge.setCurrentCost(cost);
        stream.onNext(recharge);
    }

}
