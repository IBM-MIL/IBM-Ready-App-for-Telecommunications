/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.recharge;

import com.ibm.mil.readyapps.telco.utils.PlanConstants;

import rx.Observable;

/**
 * Methods needed to be implemented by classes implementing RechargeModel.
 * Detailed method comments in RechargeModelImpl.java
 */
interface RechargeModel {
    Observable<Recharge> getRecharge(@PlanConstants.Type int type, String title);

    void changeAmount(int amount);

    void changeCost(int cost);
}
