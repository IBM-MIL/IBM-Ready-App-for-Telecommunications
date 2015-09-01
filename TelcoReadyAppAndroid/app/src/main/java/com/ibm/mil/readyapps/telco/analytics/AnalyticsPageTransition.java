/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.analytics;

import java.sql.Timestamp;

/**
 * NOTE: this class is for Gson serialization.
 *
 * AnalyticsPageTransition is a class that contains the fields necessary in tracking page
 * transitions on Operational Analytics and is used by gson for to serialize into a json object.
 * This is why it does not contain any getters or setters.
 */
public class AnalyticsPageTransition {
    long timestamp;
    String srcPage;
    String dstPage;

    public AnalyticsPageTransition(String srcPage, String dstPage, Timestamp curTime) {
        this.srcPage = srcPage;
        this.dstPage = dstPage;
        this.timestamp = curTime.getTime();
    }
}
