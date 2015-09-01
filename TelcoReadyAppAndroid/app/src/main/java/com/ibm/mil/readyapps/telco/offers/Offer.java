/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.offers;

import com.ibm.mil.readyapps.telco.utils.Currency;
import com.ibm.mil.readyapps.telco.utils.PlanConstants;

/**
 * Representation of an offer available to the user.
 */
public class Offer {

    private String title;
    private String body;
    private String termsConditions;
    private int cardIcon;
    private double cost;
    private int type;
    private String icon;
    private final boolean isAppOffer;
    private final boolean isUnlimited;
    private final boolean affectsBaseCost;
    private boolean isShareCard = false;
    private int amountAddedToCycle = 0;
    private String appName;
    private float usage;

    /**
     * Constructor initializing an offer with given fields
     *
     * @param title of the offer
     * @param body description of the offer
     * @param cardIcon image to display after the offer is accepted
     * @param cost of the offer
     * @param icon image to display with the offer
     * @param isAppOffer boolean to indicate this is an app specific offer
     * @param isUnlimited boolean to indicate this is an unlimited offer
     * @param affectsBaseCost boolean to indicate if this offer affects user's base plan cost
     */
    public Offer(String title, String body, int cardIcon, double cost, String icon, boolean isAppOffer, boolean isUnlimited, boolean affectsBaseCost) {
        this.title = title;
        this.body = body;
        this.termsConditions = "";
        this.cardIcon = cardIcon;
        this.cost = cost;
        this.icon = icon;
        this.isAppOffer = isAppOffer;
        this.isUnlimited = isUnlimited;
        this.affectsBaseCost = affectsBaseCost;
    }

    /**
     * Constructor initializing an offer with a given offer to make it a twitter share card
     *
     * @param anotherOffer the original offer
     */
    public Offer(Offer anotherOffer) {
        this.title = anotherOffer.title;
        this.body = anotherOffer.body;
        this.cardIcon = anotherOffer.cardIcon;
        this.cost = anotherOffer.cost;
        this.type = anotherOffer.type;
        this.icon = anotherOffer.icon;
        this.isAppOffer = anotherOffer.isAppOffer;
        this.isUnlimited = anotherOffer.isUnlimited();
        this.affectsBaseCost = anotherOffer.affectsBaseCost;
        this.isShareCard = anotherOffer.isShareCard;
    }

    /**
     * @return icon for the offer
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Set the icon for the offer
     *
     * @param icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return cardIcon for twitter share
     */
    public int getCardIcon() {
        return cardIcon;
    }

    /**
     * Set the icon after accepting an offer (twitter share)
     *
     * @param cardIcon share icon
     */
    public void setCardIcon(int cardIcon) {
        this.cardIcon = cardIcon;
    }

    /**
     * Get offer type
     *
     * @return type
     */
    public @PlanConstants.Type int getType() {
        return type;
    }

    /**
     * Set offer type
     *
     * @param type to set
     */
    public void setType(@PlanConstants.Type int type) {
        this.type = type;
    }

    /**
     * @return whether the offer is unlimited or not
     */
    public boolean isUnlimited() {
        return isUnlimited;
    }

    /**
     * @return whether the offer affects base cost or not
     */
    public boolean doesAffectBaseCost() {
        return affectsBaseCost;
    }

    /**
     * @return whether the offer is an app specific offer or not
     */
    public boolean isAppOffer() {
        return isAppOffer;
    }

    /**
     * Get offer title
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set offer title
     *
     * @param title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get offer description
     *
     * @return body
     */
    public String getBody() {
        return body;
    }

    /**
     * Set offer description
     *
     * @param body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Get terms and conditions text
     *
     * @return termsConditions
     */
    public String getTermsConditions() {
        return termsConditions;
    }

    /**
     * @return cost of the offer
     */
    public double getCost() {
        return cost;
    }

    /**
     */
    public void setCostZero() {
        this.cost = 0.0;
    }

    /**
     * Get the appropriate monetary unit based on user's locale
     *
     * @return the localized cost
     */
    public String getLocalizedCost() {
        return Currency.localize(cost, true);
    }

    /**
     * @return boolean indicating if the offer is shared
     */
    public boolean isShareCard() {
        return isShareCard;
    }

    /**
     */
    public void setShareCard() {
        this.isShareCard = true;
    }

    /**
     * @return the amount that has been added to the cycle
     */
    public int getAmountAddedToCycle() {
        return amountAddedToCycle;
    }

    /**
     * Set amount added to the cycle
     * @param amountAddedToCycle amount to set
     */
    public void setAmountAddedToCycle(int amountAddedToCycle) {
        this.amountAddedToCycle = amountAddedToCycle;
    }

    /**
     * Get the name of the application (for app offers)
     * @return appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * Get usage of the app if app offer
     *
     * @return usage
     */
    public float getUsage() {
        return usage;
    }
}
