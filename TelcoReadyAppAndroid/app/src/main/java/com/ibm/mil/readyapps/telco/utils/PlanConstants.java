/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.utils;

import android.support.annotation.IntDef;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.activities.MainActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Helper class containing all constants related to a user's plan
 */
public final class PlanConstants {

    public static final int DATA = 1;
    public static final int TALK = 2;
    public static final int TEXT = 3;
    public static final String DATA_UNIT = MainActivity.context.getResources().getString(R.string.data_units); // gigabytes
    public static final String TALK_UNIT = MainActivity.context.getResources().getString(R.string.talk_units); // minutes
    public static final String TEXT_UNIT = MainActivity.context.getResources().getString(R.string.text_units); // texts
    public static final int MIN_UNIT = 1;
    public static final int INITIAL_DATA_AMOUNT = 5; // in gigabytes
    public static final int INITIAL_TALK_AMOUNT = 500; // in minutes
    public static final int INITIAL_TEXT_AMOUNT = 1000; // in texts
    public static final int DATA_DOLLARS_PER_STEP = 5;
    public static final int DATA_STEP_AMOUNT = 1; // in gigabytes
    public static final int DATA_MAX_AMOUNT = 20;
    public static final int TALK_DOLLARS_PER_STEP = 1;
    public static final int TALK_STEP_AMOUNT = 25; // in minutes
    public static final int TALK_MAX_AMOUNT = 2000;
    public static final int TEXT_DOLLARS_PER_STEP = 2;
    public static final int TEXT_STEP_AMOUNT = 50; // in texts
    public static final int TEXT_MAX_AMOUNT = 2000;
    public static final int INITIAL_BASE_COST =
            (INITIAL_DATA_AMOUNT / DATA_STEP_AMOUNT * DATA_DOLLARS_PER_STEP) +
                    (INITIAL_TALK_AMOUNT / TALK_STEP_AMOUNT * TALK_DOLLARS_PER_STEP) +
                    (INITIAL_TEXT_AMOUNT / TEXT_STEP_AMOUNT * TEXT_DOLLARS_PER_STEP);
    public static final int INITIAL_ADDON_COST = 5;
    public static final int INITIAL_USED_DATA = 2;
    public static final int INITIAL_USED_TALK = 125;
    public static final int INITIAL_USED_TEXT = 350;

    private PlanConstants() {
        throw new AssertionError(PlanConstants.class.getName() + " is non-instantiable");
    }

    @IntDef({DATA, TALK, TEXT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

}
