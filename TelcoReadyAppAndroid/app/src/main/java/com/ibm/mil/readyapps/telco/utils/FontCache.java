/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Helper class for keeping fonts cached so we don't need to
 * keep creating fonts from assets every time we need a font.
 */
public class FontCache {

    private static final Hashtable<String, Typeface> fontCache = new Hashtable<>();

    /**
     * Retrieve a font from the cache if it has already been created,
     * otherwise create the font from assets.
     *
     * @param name the name of the font
     * @param context the context to get font from
     * @return the font retrieved either from cache or creation
     */
    public static Typeface get(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(name, tf);
        }

        return tf;
    }

}
