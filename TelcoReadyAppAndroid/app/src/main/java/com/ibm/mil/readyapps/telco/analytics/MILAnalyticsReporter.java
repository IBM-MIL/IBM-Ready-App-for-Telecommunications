package com.ibm.mil.readyapps.telco.analytics;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.model.Marker;
import com.ibm.mil.readyapps.telco.TelcoApplication;

/**
 * MILAnalyticsReporter is a class which is used to track analytics from the IBM Mobile
 * Innovation Lab perspective opposed to the Telco perspective.
 */
public final class MILAnalyticsReporter {

    private static String currentScreen;

    public static void hotSpotClick(Marker marker) {
        Tracker tracker = TelcoApplication.tracker;

        tracker.setScreenName(currentScreen);

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction("click")
                .setLabel("wifi hotspot marker")
                .build());
    }

    /**
     * When a user switches screens in the app, this method is triggered and logs the current
     * screen that the user is on.
     *
     * @param currentPage The current AnalyticsCnsts.Page that the user is viewing.
     */
    public static void setCurrentScreen(@AnalyticsCnsts.Page String currentPage) {
        TelcoApplication.tracker.setScreenName(currentPage);
        TelcoApplication.tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void gestureLogger(String gesture, String gestureDescription){
        Tracker tracker = TelcoApplication.tracker;
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction(gesture)
                .setLabel(gestureDescription)
                .build());
    }
   }


