/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.baseplan;

import com.ibm.mil.readyapps.telco.utils.PlanConstants;

/**
 * Representation of the user's plan.
 */
public class BasePlan {

    private double baseCost;
    private double addonCost;

    /**
     * Constructor initializing the base plan with known constants.
     */
    public BasePlan() {
        this.baseCost = PlanConstants.INITIAL_BASE_COST;
        this.addonCost = PlanConstants.INITIAL_ADDON_COST;
    }

    /**
     * Get the base cost of the user's plan.
     *
     * @return the base cost
     */
    public double getBaseCost() {
        return baseCost;
    }

    /**
     * Set the base cost of user's plan.
     *
     * @param baseCost the amount to set as base cost
     */
    public void setBaseCost(double baseCost) {
        this.baseCost = baseCost;
    }

    /**
     * Get the addon cost for just the current cycle.
     *
     * @return the additional cost for this cycle
     */
    public double getAddonCost() {
        return addonCost;
    }

    /**
     * Set the adddon cost for this cycle.
     *
     * @param addonCost the amount to set as the addon cost
     */
    public void setAddonCost(double addonCost) {
        this.addonCost = addonCost;
    }

    /**
     * Combine base cost with addon cost to get total.
     *
     * @return the total cost of plan this month
     */
    public double getTotalCost() {
        return baseCost + addonCost;
    }

}
