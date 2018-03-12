package com.ibm.mil.readyapps.telco;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.ibm.mil.readyapps.telco.utils.TelcoChallengeHandler;
import com.worklight.wlclient.api.WLClient;



public class TelcoApplication extends Application {

    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    @Override public void onCreate() {
        super.onCreate();

        analytics = GoogleAnalytics.getInstance(this);
        String analyticsKey = getString(R.string.analyticsKey);
        tracker = analytics.newTracker(analyticsKey);
        tracker.enableAutoActivityTracking(true);

        //Initialize the MobileFirst SDK. This needs to happen just once.
        WLClient.createInstance(this);
        //Initialize the challenge handler
        TelcoChallengeHandler.createAndRegister();
    }

}
