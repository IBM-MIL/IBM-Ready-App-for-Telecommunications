/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.mydata;

import com.ibm.mil.readyapps.telco.cycles.Cycle;
import com.ibm.mil.readyapps.telco.offers.Offer;
import com.ibm.mil.readyapps.telco.usage.Usage;

import java.util.List;

/**
 * Methods needed to be implemented by classes implementing DataView.
 * Detailed method comments in DataFragment.java
 */
interface DataView {

    void displayCycle(Cycle cycle);

    void displayAppUsages(List<Usage> appUsages);

    void displayAppOffers(List<Offer> appOffers);

    void displayAcceptedOffers(List<Offer> offers);

    void displayCardOffer(Offer offer);

    void removeAcceptedOffer(Offer offer);

    void displayNewUsage(Usage newUsage);

    void updateCycleView(Cycle cycle);

    void displayAppOffer(Offer offer);

    void removeAppOffer(Offer offer);

}
