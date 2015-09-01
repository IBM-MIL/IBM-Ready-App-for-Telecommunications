package com.ibm.mil.readyapps.telco;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.ibm.mil.readyapps.telco.utils.TwitterHelper;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class TelcoApplication extends Application {

    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    @Override public void onCreate() {
        super.onCreate();

        String twitterKey = TwitterHelper.getKey(getApplicationContext());
        String twitterSecret = TwitterHelper.getSecret(getApplicationContext());
        TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterKey, twitterSecret);
        Fabric.with(this, new Twitter(authConfig));

        analytics = GoogleAnalytics.getInstance(this);
        String analyticsKey = getString(R.string.analyticsKey);
        tracker = analytics.newTracker(analyticsKey);
        tracker.enableAutoActivityTracking(true);
    }

}
