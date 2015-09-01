/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.recharge;

import android.support.annotation.DrawableRes;

/**
 * Model POJO for keeping up with recharges for talk/text/data.
 */
public class Recharge {
    private final Amount amount;
    private final MetaData metaData;

    private int currentAmount;
    private int currentCost;
    private final int costStep;

    /**
     * Initialize a recharge with the required params.
     *
     * @param amount    the amount containing the initial amount and
     *                  amount to step up each time an increase occurs
     * @param costStep  the step amount for cost increases
     * @param metaData  data containing the title, units, and icon for
     *                  this recharge
     */
    public Recharge(Amount amount, int costStep, MetaData metaData) {
        this.amount = amount;
        this.costStep = costStep;
        this.metaData = metaData;

        currentAmount = amount.initial;
        currentCost = costStep;
    }

    /**
     * Retrieve the initial amount of the recharge.
     *
     * @return the initial amount
     */
    public int getInitialAmount() {
        return amount.initial;
    }

    /**
     * Retrieve how much an increase should increase
     * the amount.
     *
     * @return the step amount
     */
    public int getAmountStep() {
        return amount.step;
    }

    /**
     * Get the step amount for the cost for each increase.
     *
     * @return the cost step
     */
    public int getCostStep() {
        return costStep;
    }

    /**
     * Get the current amount of the recharge.
     * e.g. 3GB
     *
     * @return the current amount
     */
    public int getCurrentAmount() {
        return currentAmount;
    }

    /**
     * Set the current amount of the recharge.
     * e.g. 3GB
     *
     * @param newAmount the amount value to set
     */
    public void setCurrentAmount(int newAmount) {
        currentAmount = newAmount;
    }

    /**
     * Get the current cost of the recharge.
     *
     * @return the current cost
     */
    public int getCurrentCost() {
        return currentCost;
    }

    /**
     * Set the current cost of the recharge.
     *
     * @param newCost the new cost
     */
    public void setCurrentCost(int newCost) {
        currentCost = newCost;
    }

    /**
     * Get the title of recharge
     *
     * @return the title
     */
    public String getTitle() {
        return metaData.title;
    }

    /**
     * Get the units of recharge.
     * e.g. 'GB'
     *
     * @return the units
     */
    public String getUnits() {
        return metaData.units;
    }

    /**
     * Get the icon for the recharge.
     *
     * @return the icon
     */
    public @DrawableRes
    int getDrawableIcon() {
        return metaData.icon;
    }

    /**
     * Class for holding the metadata of a recharge.
     * i.e. title and units of the recharge
     */
    public static class MetaData {
        private final String title;
        private final String units;
        @DrawableRes private final int icon;

        public MetaData(String title, String units, @DrawableRes int icon) {
            this.title = title;
            this.units = units;
            this.icon = icon;
        }
    }

    /**
     * Class for holding recharge amount that encapsulates
     * the initial amount and the step amount for the
     * recharge
     */
    public static class Amount {
        private final int initial;
        private final int step;

        public Amount(int initial, int step) {
            this.initial = initial;
            this.step = step;
        }
    }

}
