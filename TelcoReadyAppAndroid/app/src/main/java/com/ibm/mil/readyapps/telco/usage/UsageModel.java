/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.usage;

import android.content.Context;

import com.ibm.mil.readyapps.telco.offers.Offer;

import rx.Observable;

/**
 * Methods for getting, setting and updating usage fields
 */
public interface UsageModel {
    Observable<Usage> getUsages(Context context);

    Observable<Usage> getTalkUsage(Context context);

    Observable<Usage> getTextUsage(Context context);

    void setNewUsage(Usage newUsage);

    Usage setLimitedUsage(Offer offer);
}
