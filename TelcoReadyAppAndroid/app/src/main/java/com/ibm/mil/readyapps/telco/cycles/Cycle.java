/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.cycles;

import com.ibm.mil.readyapps.telco.utils.PlanConstants;

/**
 * Representation of the user's plan cycle.
 */
public class Cycle {

    private final int cycleImage;
    private float used;
    private int limit;
    private final String unit;
    private @PlanConstants.Type final int type;

    /**
     * Constructor for initializing a cycle instance
     *
     * @param cycleImage the image for this cycle
     * @param used the amount (data/talk/text) used for this cycle
     * @param limit the limit for this cycle
     * @param unit the unit for the data/text/talk cycle
     * @param type specifies the type of the cycle(data/text/talk)
     */
    public Cycle(int cycleImage, int used, int limit, String unit,
                 @PlanConstants.Type int type) {
        this.cycleImage = cycleImage;
        this.used = used;
        this.limit = limit;
        this.unit = unit;
        this.type = type;
    }

    /**
     * Get the image associated with this cycle instance
     *
     * @return the cycle image
     */
    public int getCycleImage() {
        return cycleImage;
    }

    /**
     * Get the used data for this cycle
     *
     * @return the used data amount
     */
    public float getUsed() {
        return used;
    }

    /**
     * Set the used data for this cycle
     *
     * @param used to set
     */
    public void setUsed(float used) {
        this.used = used;
    }

    /**
     * Get the limit for this cycle
     *
     * @return the limit amount
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Set the limit for this cycle
     * @param limit amount of the limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Get unit of this cycle
     *
     * @return unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Get type of this cycle
     *
     * @return type
     */
    public @PlanConstants.Type int getType() {
        return type;
    }

}