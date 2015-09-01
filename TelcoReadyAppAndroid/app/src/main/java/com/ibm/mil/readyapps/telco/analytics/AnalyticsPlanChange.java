/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.analytics;

import java.sql.Timestamp;

/**
 * NOTE: this class is for Gson serialization.
 *
 * AnalyticsPlanChange is a class that contains the fields necessary for tracking when a user
 * accepts a plan change (ie accepting an offer) for Operational Analytics. This class is only for
 * gson to serialize into a json object. This is why it does not contain any getters or setters.
 */
public class AnalyticsPlanChange {
    String planChangeType;
    double planChangeCost;
    long timestamp;

    public AnalyticsPlanChange(String planChangeType, double planChangeCost, Timestamp curTime) {
        this.planChangeType = planChangeType;
        this.planChangeCost = planChangeCost;
        this.timestamp = curTime.getTime();
    }
}
