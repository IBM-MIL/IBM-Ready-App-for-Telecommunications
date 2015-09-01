/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.utils;

import java.text.NumberFormat;

/**
 * Helper class for localizing currency symbols.
 */
public class Currency {

    /**
     * Method for localizing an amount to include a string with correct currency symbol
     * based on user's language settings on device.
     *
     * @param amount the amount wanted to localize (e.g. 5 for $5)
     * @param showDecimalPlaces whether or not to show decimal places in returned string
     * @return the localized amount showing the correct currency symbol
     */
    public static String localize(double amount, boolean showDecimalPlaces) {
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        int decimalPlaces = showDecimalPlaces ? 2 : 0;
        defaultFormat.setMinimumFractionDigits(decimalPlaces);
        defaultFormat.setMaximumFractionDigits(2);
        return defaultFormat.format(amount);
    }

}
