/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.recharge;

/**
 * Methods needed to be implemented by classes implementing RechargeView.
 * Detailed method comments in RechargeFragment.java
 */
interface RechargeView {

    void updateTextViews(Recharge recharge);

    void close();

}
