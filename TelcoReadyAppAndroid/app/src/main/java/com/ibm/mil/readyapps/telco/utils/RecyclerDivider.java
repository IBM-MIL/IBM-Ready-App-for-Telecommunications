/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.utils;

/**
 * Helper class used as divider for Offer model array list in tab fragments.
 * RecyclerDividers separate different groups of items in recycler lists on
 * data, talk, and text tabs.
 */
public class RecyclerDivider {

    private final String dividerText;
    private final int dividerBGcolor;

    /**
     * Initialize a RecyclerDivider object with text and background color
     *
     * @param dividerText the text to be shown in divider
     * @param dividerBGcolor the background color of the divider
     */
    public RecyclerDivider(String dividerText, int dividerBGcolor) {
        this.dividerText = dividerText;
        this.dividerBGcolor = dividerBGcolor;
    }

    /**
     * Initialize a RecyclerDivider object with only text
     *
     * @param dividerText the text for the divider
     */
    public RecyclerDivider(String dividerText) {
        this.dividerText = dividerText;
        this.dividerBGcolor = -1;
    }

    /**
     * Get the text for the divider
     *
     * @return the divider text
     */
    public String getDividerText() {
        return dividerText;
    }

    /**
     * Get the background color of divider
     *
     * @return the background color
     */
    public int getDividerBGcolor() {
        return dividerBGcolor;
    }

}
