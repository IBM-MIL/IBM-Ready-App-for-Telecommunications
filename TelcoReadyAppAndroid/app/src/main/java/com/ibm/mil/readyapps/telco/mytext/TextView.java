/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.mytext;

import com.ibm.mil.readyapps.telco.cycles.Cycle;
import com.ibm.mil.readyapps.telco.offers.Offer;
import com.ibm.mil.readyapps.telco.usage.Usage;

import java.util.List;

/**
 * Methods needed to be implemented by classes implementing TextView.
 * Detailed method comments in TextFragment.java
 */
interface TextView {
    void displayCycle(Cycle cycle);

    //includes incoming and outgoing
    void displayUsage(Usage usage);

    void displayAcceptedOffers(List<Offer> offers);

    void updateCycleView(Cycle cycle);

    void removeAcceptedOffer(Offer offer);

    void displayAcceptedOffer(Offer offer);
}
