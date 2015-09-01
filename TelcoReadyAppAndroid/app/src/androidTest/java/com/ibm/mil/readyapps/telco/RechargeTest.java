/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco;

import android.os.SystemClock;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.TextView;

import com.ibm.mil.readyapps.telco.activities.MainActivity;

import org.hamcrest.Matcher;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;


/**
 * This test class the Recharge page
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RechargeTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);
    List<String> stringsToSearchFor = new ArrayList<>(
            Arrays.asList(
                    "WiFi Finder"
            )
    );

    String dataTabText = "Data";
    String talkTabText = "Talk";
    String textTabText = "Text";

    Integer dataInterval = 1;
    Integer talkInterval = 25;
    Integer textInterval = 50;

    /**
     * Gets the text from a TextView
     * @param matcher The matched view to extract text from
     * @return The string in the view
     */
    String getText(final Matcher<View> matcher) {
        final String[] stringHolder = {null};
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView) view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

    /**
     * Tests the data recharge page. Increments and decrements amount of data and checks that
     * the value changes accordingly.
     */
    @Test
    public void testDataRecharge() {
        onView(withChild(withText(dataTabText))).perform(click());
        onView(withId(R.id.main_activity_fab)).perform(click());

        // Get the starting amount of data
        String initialDataAmount = getText(allOf(withId(R.id.unit_value)));
        int initialData = Integer.parseInt(initialDataAmount);

        // Increment amount of data and check values
        int finalData = initialData + (dataInterval * 2);
        String finalDataString = Integer.toString(finalData);
        onView(withId(R.id.up_arrow)).perform(click());
        onView(withId(R.id.up_arrow)).perform(click());
        onView(withId(R.id.unit_value)).check(matches(withText(finalDataString)));

        // Decrement amount of data and check values
        finalData = finalData - dataInterval;
        finalDataString = Integer.toString(finalData);
        onView(withId(R.id.down_arrow)).perform(click());
        onView(withId(R.id.unit_value)).check(matches(withText(finalDataString)));
    }

    /**
     * Tests the talk recharge page. Increments and decrements amount of minutes and checks that
     * the values change accordingly.
     */
    @Test
    public void testTalkRecharge() {
        onView(withChild(withText(talkTabText))).perform(click());
        onView(withId(R.id.main_activity_fab)).perform(click());

        // Get the starting amount of minutes
        String initialMinutesAmount = getText(allOf(withId(R.id.unit_value)));
        int initialMinutes = Integer.parseInt(initialMinutesAmount);

        // Increment amount of minutes and check values
        int finalMinutes = initialMinutes + (talkInterval * 2);
        String finalMinutesString = Integer.toString(finalMinutes);
        onView(withId(R.id.up_arrow)).perform(click());
        onView(withId(R.id.up_arrow)).perform(click());
        onView(withId(R.id.unit_value)).check(matches(withText(finalMinutesString)));

        // Decrement amount of minutes and check values
        finalMinutes = finalMinutes - talkInterval;
        finalMinutesString = Integer.toString(finalMinutes);
        onView(withId(R.id.down_arrow)).perform(click());
        onView(withId(R.id.unit_value)).check(matches(withText(finalMinutesString)));
    }

    /**
     * Tests the text recharge page. Increments and decrements amount of texts and checks that
     * the values change accordingly.
     */
    @Test
    public void testTextRecharge() {
        onView(withChild(withText(textTabText))).perform(click());
        onView(withId(R.id.main_activity_fab)).perform(click());

        // Get the starting amount of texts
        String initialTextsAmount = getText(allOf(withId(R.id.unit_value)));
        int initialTexts = Integer.parseInt(initialTextsAmount);

        // Increment amount of texts and check values
        int finalTexts = initialTexts + (textInterval * 2);
        String finalTextsString = Integer.toString(finalTexts);
        onView(withId(R.id.up_arrow)).perform(click());
        onView(withId(R.id.up_arrow)).perform(click());
        onView(withId(R.id.unit_value)).check(matches(withText(finalTextsString)));

        // Decrement amount of texts and check values
        finalTexts = finalTexts - textInterval;
        finalTextsString = Integer.toString(finalTexts);
        onView(withId(R.id.down_arrow)).perform(click());
        onView(withId(R.id.unit_value)).check(matches(withText(finalTextsString)));
    }
}