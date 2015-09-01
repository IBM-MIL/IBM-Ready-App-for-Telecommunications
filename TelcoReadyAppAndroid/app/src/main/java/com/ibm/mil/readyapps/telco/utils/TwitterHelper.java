/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ibm.mil.readyapps.telco.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

/**
 * Helper class for sending out tweets on user's behalf and getting authorization from user.
 */
public final class TwitterHelper {

    private static final String TAG = "TWITTER_HELPER";

    /**
     * This class has only static methods and should not be instantiated.
     */
    private TwitterHelper() {
        throw new AssertionError(TwitterHelper.class.getName() + " is non-instantiable");
    }

    /**
     * Update the user's twitter status.
     *
     * @param status the status to tweet
     */
    public static void tweet(String status) {
        StatusesService statusesService = TwitterCore.getInstance().getApiClient().getStatusesService();
        statusesService.update(status, null, null, null, null, null, null, null, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Log.d(TAG, "UPDATING STATUS SUCCESS");
            }

            @Override
            public void failure(TwitterException e) {
                Log.d(TAG, "UPDATING STATUS FAILURE");
            }
        });
    }

    /**
     * Gain authorization from the user to tweet on their behalf, and then send the tweet.
     *
     * @param activity          the activity reference needed by TwitterAuthClient.authorize
     * @param twitterAuthClient the auth client passed in from MainActivity needed so the result
     *                          of authorization can be reported
     * @param status            the status to tweet
     */
    public static void authorizeThenTweet(Activity activity, TwitterAuthClient twitterAuthClient, final String status) {
        twitterAuthClient.authorize(activity, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                tweet(status);
            }

            @Override
            public void failure(TwitterException e) {
                Log.d(TAG, "AUTHORIZATION FAILURE");
            }
        });
    }

    /**
     * Determine if the user already has an active Twitter session with the app.
     *
     * @return true if already authorized, false otherwise
     */
    public static boolean alreadyAuthorized() {
        return Twitter.getSessionManager().getActiveSession() != null;
    }

    /**
     * Get the Twitter API key needed to instantiate a twitter auth config.
     *
     * @return the decoded Twitter key
     */
    public static String getKey(Context context) {
        String encodedTwitterKey = context.getString(R.string.encodedTwitterKey);
        return StringObfuscator.decode(encodedTwitterKey);
    }

    /**
     * Get the Twitter API secret needed to instantiate a twitter auth config.
     *
     * @return the decoded Twitter secret
     */
    public static String getSecret(Context context) {
        String encodedTwitterSecret = context.getString(R.string.encodedTwitterSecret);
        return StringObfuscator.decode(encodedTwitterSecret);
    }

}
