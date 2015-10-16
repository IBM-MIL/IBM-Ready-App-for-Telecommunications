/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */


package com.ibm.mil.readyapps.telco;

import android.os.SystemClock;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import com.ibm.mil.readyapps.telco.activities.MainActivity;

import org.hamcrest.Matcher;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

/**
 * This test class has tests for various UI features on the My Plan page of the application.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MyPlanTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    /**
     * This test checks that the appropriate views are on the My Plan page
     */
    @Test
    public void testMyPlanText() throws Exception {
        onView(withId(R.id.daysLeftDynamic)).check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * This test checks for an offer on the My Plan page, accepts the offer, then checks to see
     * if the offer is removed from the screen
     */
    public void testAcceptOffer() {
        SystemClock.sleep(1000);
        Matcher<View> cardTitleView = allOf(withId(R.id.offer_title), withText("Recharge Data"));
        onView(allOf(withId(R.id.accept), hasSibling(cardTitleView))).perform(click());
        onView(cardTitleView).check(ViewAssertions.doesNotExist());
    }
}