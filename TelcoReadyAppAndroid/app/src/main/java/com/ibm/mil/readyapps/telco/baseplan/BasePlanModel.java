/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.baseplan;

import rx.Observable;

/**
 * Methods needed to be implemented by classes implementing BasePlanModel.
 * Detailed method comments in BaseModelImpl.java
 */
public interface BasePlanModel {

    Observable<BasePlan> getBasePlanStream();

    void updateBaseCost(double changeAmount);

    void updateAddonCost(double changeAmount);

    void updateCost(double changeAmount, boolean affectsBaseCost);

}
