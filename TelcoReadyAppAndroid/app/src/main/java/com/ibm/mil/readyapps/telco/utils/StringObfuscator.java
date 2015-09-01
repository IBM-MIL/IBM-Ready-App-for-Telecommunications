/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.utils;

import android.util.Base64;
import android.util.Log;

/**
 * Helper class for decoding a string using Base64 encryption
 */
final class StringObfuscator {

    private static final String TAG = "STRING_OBFUSCATOR";

    /**
     * This class has only static methods and properties so shouldn't be be instantiated.
     */
    private StringObfuscator() {
        throw new AssertionError(StringObfuscator.class.getName() + " is non-instantiable");
    }

    /**
     * Do the actual decoding of the string using base64 encryption
     *
     * @param encodedString the encoded string to be decoded
     * @return the decoded string
     */
    public static String decode(String encodedString) {
        String decodedString = "";

        if (encodedString.equals("your_encoded_twitter_key") || encodedString.equals("your_encoded_twitter_secret")) {
            return decodedString;
        }

        byte[] data = Base64.decode(encodedString, Base64.DEFAULT);

        try {
            decodedString = new String(data, "UTF-8");
        } catch (Exception e) {
            Log.d(TAG, "DECODING STRING FAILURE");
        }

        return decodedString;
    }

}
