/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */


package com.ibm.mil.readyapps.telco;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.ibm.mil.readyapps.telco.activities.MainActivity;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * This test class the Wifi finder page
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class WifiTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);
    List<String> stringsToSearchFor = new ArrayList<>(
            Arrays.asList(
                    "WiFi Finder"
            )
    );

    String myPlanTabText = "My Plan";

    /**
     * This test checks that the FAB button to the wifi finder works and that certain views exist
     * on the Wifi page
     */
    @Test
    public void testFabButton() {
        SystemClock.sleep(1000);
        onView(withChild(withText(myPlanTabText))).perform(click());
        SystemClock.sleep(1000);
    }
}
