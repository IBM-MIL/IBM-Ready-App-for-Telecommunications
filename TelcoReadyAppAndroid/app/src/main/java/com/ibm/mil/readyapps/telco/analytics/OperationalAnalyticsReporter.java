/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.analytics;

import android.util.Log;

import com.google.gson.Gson;
import com.worklight.common.WLAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;

public final class OperationalAnalyticsReporter {

    private static final Gson gson = new Gson();

    /**
     * planChangeAccepted() sends an Operational Analytics log anytime a user accepts a plan change.
     *
     * @param planChangeType the type of plan change that a user has accepted (ie baseplan
     *                       increase, baseplan decrease, addon, etc)
     * @param amount the cost amount of the plan change.
     */
    public static void planChangeAccepted(@AnalyticsCnsts.PlanChange String planChangeType,
                                          double amount) {
        // Create a custom activity with a log message
        Date date = new Date();
        Timestamp curTime = new Timestamp(date.getTime());

        String json = gson.toJson(new AnalyticsPlanChange(planChangeType, amount, curTime),
                AnalyticsPlanChange.class);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            jsonObject.put("_activity", "planChanged");

            Log.i("TEST", "JSON AFTER ACTIVITY " + jsonObject.toString());

            WLAnalytics.log("Plan change accepted by a user", jsonObject); //async
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * pageTransition() sends an Operational Analytics log anytime a user changes from one screen
     * to another.
     *
     * @param src The source page that the user is navigating from.
     * @param dst The destination page that the user navigates to, it refers to there current
     *            screen.
     * @param dwellTime The dwellTime in milliseconds that the user spent on the source page
     *                  before transitioning to the destination page.
     */
    public static void pageTransition(@AnalyticsCnsts.Page String src, @AnalyticsCnsts.Page
            String dst, long dwellTime) {
        Date date = new Date();
        Timestamp curTime = new Timestamp(date.getTime());

        String json = gson.toJson(new AnalyticsPageTransition(src, dst, curTime),
                AnalyticsPageTransition.class);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            jsonObject.put("_activity", "pageTransition");
            String dwellField = src + "_dwellTime";
            double dwellTimeSeconds = (double) dwellTime / 1000;
            jsonObject.put(dwellField, dwellTimeSeconds );

            Log.i("TEST", "PAGE_TRANSITION : " + jsonObject.toString());


            WLAnalytics.log("Page transition recorded.", jsonObject);

            WLAnalytics.send();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
