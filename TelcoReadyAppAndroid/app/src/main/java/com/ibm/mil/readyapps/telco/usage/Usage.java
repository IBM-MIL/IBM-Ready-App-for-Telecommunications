/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.usage;

import com.ibm.mil.readyapps.telco.offers.Offer;
import com.ibm.mil.readyapps.telco.utils.PlanConstants;

public class Usage {
    private int total;
    private String appName;
    private int usageImage;
    private int seekBarProgress;
    private String usedData;
    private double used;
    private int limit;
    private boolean isUnlimited;
    private @PlanConstants.Type int type;
    private int incoming;
    private int outgoing;
    private String imageName;

    /**
     * Constructor initializing a usage with given fields
     *
     * @param total units used
     * @param isUnlimited boolean to indicate if usage is Unlimited
     * @param type of the usage
     * @param incoming units used (minutes or sms)
     * @param outgoing units used (minutes or sms)
     */
    public Usage(int total, boolean isUnlimited, @PlanConstants.Type int type, int incoming,
                 int outgoing) {
        this.total = total;
        this.isUnlimited = isUnlimited;
        this.type = type;
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    /**
     * Constructor initializing a usage with given fields
     *
     * @param appName name of the application for the usage
     * @param usageImage image to display with the usage
     * @param used units already used
     * @param isUnlimited boolean to indicate if usage is Unlimited
     * @param type of the usage
     */
    public Usage(String appName, int usageImage, double used, Boolean isUnlimited, @PlanConstants.Type int type) {
        this.appName = appName;
        this.usageImage = usageImage;
        this.used = used;
        this.usedData = usedData;
        this.isUnlimited = isUnlimited;
        this.type = type;
    }

    //empty constructor
    public Usage() {
    }

    /**
     * Convert an Offer object to a Usage object
     * @param offer to convert
     * @return usage created from offer
     */
    public Usage offerToUsage(Offer offer) {
        return new Usage(offer.getAppName(), offer.getCardIcon(), getUsed(), offer.isUnlimited(),
            offer.getType());
    }

    /**
     * Set whether an offer is unlimited
     * @param isUnlimited boolean
     */
    public void setIsUnlimited(boolean isUnlimited) {
        this.isUnlimited = isUnlimited;
    }

    /**
     * @return total used units
     */
    public int getTotal() {
        return total;
    }

    /**
     * @return limit per user base plan
     */
    public int getLimit() {
        return limit;
    }

    /**
     * @param limit to set for user base plan
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * @return whether the offer is unlimited or not
     */
    public boolean isUnlimited() {
        return isUnlimited;
    }

    /**
     * Get the name of the application (for app offers)
     * @return appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @return icon for the usage
     */
    public int getUsageImage() {
        return usageImage;
    }

    /**
     * @return icon name for the usage
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * @return seekBarProgress for the usage
     */
    public int getSeekBarProgress() {
        return seekBarProgress;
    }

    /**
     * Set seekBarProgress for the usage
     * @param seekBarProgress to set
     */
    public void setSeekBarProgress(int seekBarProgress) {
        this.seekBarProgress = seekBarProgress;
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
     * Set the icon for the usage
     *
     * @param usageImage to set
     */
    public void setUsageImage(int usageImage) {
        this.usageImage = usageImage;
    }

    /**
     * @return incoming amount for usage
     */
    public int getIncoming() {
        return incoming;
    }

    /**
     * @return outgoing amount for usage
     */
    public int getOutgoing() {
        return outgoing;
    }

    /**
     * @return used amount for usage
     */
    public double getUsed() {
        return used;
    }

    /**
     * Set used amount for usage
     * @param used to set
     */
    public void setUsed(double used) {
        this.used = used;
    }
}