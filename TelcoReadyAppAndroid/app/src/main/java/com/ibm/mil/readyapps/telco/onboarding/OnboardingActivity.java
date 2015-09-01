/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.onboarding;

import android.content.Intent;
import android.os.Bundle;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.activities.MainActivity;
import com.ibm.mil.readyapps.telco.onboarding.appintrolib.AppIntro;
import com.ibm.mil.readyapps.telco.onboarding.appintrolib.IntroSlide;

/**
 * Activity started when first time opening the app.
 * Shows an introduction to app and how to use the app.
 */
public class OnboardingActivity extends AppIntro {

    public static final String COMING_FROM_OVERFLOW = "com.ibm.mil.readyapps.telco.comingfromoverflow";
    private boolean comingFromOverflow = false;

    /**
     * Add all the fragments to the introduction.
     *
     * @param bundle check bundle to see if user is coming to this activity
     *               because it is their first time opening app or
     *               if they tapped 'Show Walkthrough' from
     *               overflow menu
     */
    @Override
    public void init(Bundle bundle) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            comingFromOverflow = extras.getBoolean(COMING_FROM_OVERFLOW);
        }

        addSlide(IntroSlide.newInstance(R.layout.fragment_welcome_intro));
        addSlide(IntroSlide.newInstance(R.layout.fragment_cards_intro));
        addSlide(IntroSlide.newInstance(R.layout.fragment_share_intro));
        addSlide(IntroSlide.newInstance(R.layout.fragment_recharge_intro));
        addSlide(IntroSlide.newInstance(R.layout.fragment_wifi_intro));
        addSlide(IntroSlide.newInstance(R.layout.fragment_notification_intro));
        addSlide(IntroSlide.newInstance(R.layout.fragment_thanks_intro));
    }

    /**
     * If the user taps skip take them to main activity immediately.
     */
    @Override
    public void onSkipPressed() {
        goToMainActivity();
    }

    /**
     * When the user taps done take them to main activity.
     */
    @Override
    public void onDonePressed() {
        goToMainActivity();
    }

    /**
     * To get to main activity when opening the app for the first time,
     * we should start the main activity. However, if the user has
     * come to the walkthrough from the overflow menu (which means they
     * have already seen the main activity) then simply close the
     * walkthrough to get back to the main activity.
     */
    private void goToMainActivity() {
        if (comingFromOverflow) {
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

}
