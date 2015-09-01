/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.analytics;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class AnalyticsCnsts {

    /** All of the plan change types. */
    public static final String OFFER = "offer";
    public static final String BASE_INCREASE = "baseIncrease";
    public static final String BASE_DECREASE = "baseDecrease";
    public static final String ADDON = "addon";
    @StringDef({OFFER, BASE_INCREASE, BASE_DECREASE, ADDON})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PlanChange {
    }

    /** All of the tracked screens (pages) */
    public static final String MYPLAN = "myplan_page";
    public static final String MYDATA = "mydata_page";
    public static final String MYTALK = "mytalk_page";
    public static final String MYTEXT = "mytext_page";
    public static final String WIFI = "wifi_page";
    public static final String RECHARGE = "recharge_page";
    @StringDef({MYPLAN, MYDATA, MYTALK, MYTEXT, WIFI, RECHARGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Page {
    }
}
