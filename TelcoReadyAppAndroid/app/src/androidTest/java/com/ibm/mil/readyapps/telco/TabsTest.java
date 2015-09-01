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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * This test class tests the tabs and the view at the top of the application
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TabsTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    List<String> tabStrings = new ArrayList<>(
            Arrays.asList(
                    "My Plan", "Data", "Talk", "Text"
            )
    );

    /**
     * This test checks that the Tabs have the appropriate titles
     */
    @Test
    public void testTabText() {
        SystemClock.sleep(1000);
        for (String tabText : tabStrings) {
            onView(withChild(withText(tabText))).check(ViewAssertions.matches(isDisplayed()));
        }
    }

    /**
     * This test checks that the Tabs are clickable
     */
    @Test
    public void testTabsClickable() {
        SystemClock.sleep(1000);
        for (String tabText : tabStrings) {
            onView(withChild(withText(tabText))).perform(click());
        }
    }
}