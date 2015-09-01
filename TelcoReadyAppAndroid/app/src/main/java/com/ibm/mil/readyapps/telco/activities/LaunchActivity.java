/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.ibm.mil.readyapps.telco.onboarding.OnboardingActivity;

/**
 * Initial Activity launched upon start of the app.
 * It doesn't actually show anything itself, just decides
 * which Activity (Main or Onboarding) should be the
 * first Activity shown.
 */
public class LaunchActivity extends Activity {

    private static final String FIRST_APP_LAUNCH = "com.ibm.mil.readyapps.telco.firstapplaunch";

    /**
     * Only show the app onboarding (walkthrough) if this is the first
     * time running the app on the device.
     *
     * @param savedInstanceState previous state of activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFirstAppLaunch()) {
            setAppHasBeenLaunched();
            startActivity(new Intent(this, OnboardingActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }

        finish();
    }

    /**
     * Determine if this is the first app launch on the device by checking
     * shared preferences.
     *
     * @return true if this is the first app launch, false otherwise
     */
    private boolean isFirstAppLaunch() {
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        return preferences.getBoolean(FIRST_APP_LAUNCH, true);
    }

    /**
     * Save whether or not this is the first app launch in shared preferences.
     *
     */
    private void setAppHasBeenLaunched() {
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(FIRST_APP_LAUNCH, false);
        editor.apply();
    }
}
