/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.recharge;

import android.content.Context;

/**
 * Methods needed to be implemented by classes implementing RechargePresenter.
 * Detailed method comments in RechargePresenterImpl.java
 */
interface RechargePresenter {

    void createRecharge();

    void increaseAmount();

    void decreaseAmount();

    void accept(Context context);

}
