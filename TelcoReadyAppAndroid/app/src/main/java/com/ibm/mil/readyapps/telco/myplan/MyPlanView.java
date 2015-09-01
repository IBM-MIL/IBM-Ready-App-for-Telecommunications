/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.myplan;

import com.ibm.mil.readyapps.telco.baseplan.BasePlan;
import com.ibm.mil.readyapps.telco.cycles.Cycle;
import com.ibm.mil.readyapps.telco.offers.Offer;

import java.util.List;

/**
 * Methods needed to be implemented by classes implementing MyPlanView.
 * Detailed method comments in MyPlanFragment.java
 */
public interface MyPlanView {

    void displayOffers(List<Offer> offers);

    void displayTalkCycle(Cycle cycle);

    void displayTextCycle(Cycle cycle);

    void displayDataCycle(Cycle cycle);

    void displayOffer(Offer offer);

    void updateDataCycle(Cycle cycle);

    void updateTalkCycle(Cycle cycle);

    void updateTextCycle(Cycle cycle);

    void displayBasePlanInfo(BasePlan basePlan);

    void updateRemove(Offer offer);

}
