/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */


package com.ibm.mil.readyapps.telco;

import android.os.SystemClock;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibm.mil.readyapps.telco.activities.MainActivity;

import org.hamcrest.Description;
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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;


/**
 * This test class tests the Talk tab page
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TalkPageTest {
    
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    List<String> stringsToSearchFor = new ArrayList<>(
            Arrays.asList(
                    "CURRENT CYCLE", "INCOMING", "OUTGOING"
            )
    );

    String talkTabText = "Talk";

    Integer minutesInterval = 25;
    Integer minutesCostInterval = 1;

    /**
     * Gets the text from a TextView
     * @param matcher The matched view to extract text from
     * @return The string in the view
     */
    String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
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
     * This is a helper function to that the UI test case can test what the progress bar value is
     * on the SeekBars
     * @param expectedProgress The progress value between 0-100 that we expect
     * @return
     */
    public static Matcher<View> withProgress(final int expectedProgress) {
        return new BoundedMatcher<View, ProgressBar>(ProgressBar.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("expected: ");
                description.appendText(""+expectedProgress);
            }

            @Override
            public boolean matchesSafely(ProgressBar progressBar) {
                return progressBar.getProgress() == expectedProgress;
            }
        };
    }

    /**
     * This test checks for some basic views that should exist on the Data page
     */
    @Test
    public void testTalkPageViews() {
        SystemClock.sleep(1000);
        onView(withChild(withText(talkTabText))).perform(click());
        for (String text: stringsToSearchFor) {
            onView(allOf(withText(containsString(text)), isDescendantOfA(withId(R.id.my_talk_recyclerview)))).check(ViewAssertions.matches(isDisplayed()));
        }
    }

    /**
     * This test checks that you can increase and decrease the base plan values
     */
    @Test
    public void testChangingBaseTalkPlanArrows() {
        SystemClock.sleep(1000);
        onView(withChild(withText(talkTabText))).perform(click());

        // Get the starting minutes number
        String startingText = getText(allOf(withId(R.id.usage_gb), isDescendantOfA(withId(R.id.my_talk_recyclerview))));
        int startingMinutes = Integer.parseInt(startingText);

        // Increment and check
        startingMinutes += minutesInterval;
        String newString = Integer.toString(startingMinutes);
        onView(allOf(withId(R.id.up_arrow), isDescendantOfA(withId(R.id.my_talk_recyclerview)))).perform(click());
        onView(allOf(withId(R.id.usage_gb), isDescendantOfA(withId(R.id.my_talk_recyclerview)))).check(matches(withText(newString)));

        // Decrement twice and check
        startingMinutes -= (minutesInterval * 2);
        newString = Integer.toString(startingMinutes);
        onView(allOf(withId(R.id.down_arrow), isDescendantOfA(withId(R.id.my_talk_recyclerview)))).perform(click());
        onView(allOf(withId(R.id.down_arrow), isDescendantOfA(withId(R.id.my_talk_recyclerview)))).perform(click());
        onView(allOf(withId(R.id.usage_gb), isDescendantOfA(withId(R.id.my_talk_recyclerview)))).check(matches(withText(newString)));

        // Reset
        onView(allOf(withId(R.id.up_arrow), isDescendantOfA(withId(R.id.my_talk_recyclerview)))).perform(click());
    }

    /**
     * This test checks that the base plan price changes correctly
     */
    @Test
    public void testTalkBasePlanPriceChange() {
        SystemClock.sleep(1000);
        onView(withChild(withText(talkTabText))).perform(click());

        // Get the starting total cost number
        String startingText = getText(withId(R.id.current_plan_total)).split("\\/")[0];
        String currencySymbol = startingText.substring(0, 1);
        startingText = startingText.substring(1);  // Remove the currency symbol character
        int startingCost = Integer.parseInt(startingText);

        // Increase the number of minutes and confirm
        startingCost += minutesCostInterval;
        String newString = currencySymbol + startingCost;

        onView(allOf(withId(R.id.up_arrow), isDescendantOfA(withId(R.id.my_talk_recyclerview)))).perform(click());
        onView(allOf(withId(R.id.confirm), isDescendantOfA(withId(R.id.my_talk_recyclerview)))).perform(click());

        // Check the total amount
        onView(withId(R.id.current_plan_total)).check(matches(withText(newString)));

        // Reset amount
        onView(allOf(withId(R.id.down_arrow), isDescendantOfA(withId(R.id.my_talk_recyclerview)))).perform(click());
        onView(allOf(withId(R.id.confirm), isDescendantOfA(withId(R.id.my_talk_recyclerview)))).perform(click());
    }

    /**
     * Tests that the Current Cycle UI updates on the Data page after changing base data plan
     */
    @Test
    public void testTalkCurrentCycleUpdate() {
        SystemClock.sleep(1000);
        onView(withChild(withText(talkTabText))).perform(click());

        Matcher<View> usageView = allOf(withId(R.id.bottom_left_text), isDescendantOfA(withId(R.id.my_talk_recyclerview)), isDescendantOfA(withId(R.id.cycle_viewer)));
        Matcher<View> progressBar = allOf(withId(R.id.progress_bar), isDescendantOfA(withId(R.id.my_talk_recyclerview)), isDescendantOfA(withId(R.id.cycle_viewer)));

        // Get initial minute values out of String
        String usedMinutes = getText(usageView).split("\\/")[0];
        String totalMinutes = getText(usageView).split("\\/")[1].split(" ")[0];
        float usedMinutesFloat = Float.parseFloat(usedMinutes);
        int totalMinutesInt = Integer.parseInt(totalMinutes);
        int percentUsed = (int)(((double)usedMinutesFloat/ (double)totalMinutesInt) * 100.0);

        onView(progressBar).check(ViewAssertions.matches(withProgress(percentUsed)));

        // Increase the minutes and confirm changes in Current Cycle
        onView(allOf(withId(R.id.up_arrow), isDescendantOfA(withId(R.id.my_talk_recyclerview)))).perform(click());
        onView(allOf(withId(R.id.confirm), isDescendantOfA(withId(R.id.my_talk_recyclerview)))).perform(click());

        totalMinutesInt += minutesInterval;
        String newString = usedMinutesFloat+"/"+totalMinutesInt+" min";
        onView(usageView).check(matches(withText(newString)));

        percentUsed = (int)(((double)usedMinutesFloat/ (double)totalMinutesInt) * 100.0);
        onView(progressBar).check(ViewAssertions.matches(withProgress(percentUsed)));
    }
}

