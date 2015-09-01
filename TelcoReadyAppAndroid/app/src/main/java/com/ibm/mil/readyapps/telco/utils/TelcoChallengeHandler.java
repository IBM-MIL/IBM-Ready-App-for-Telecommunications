/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.utils;

import android.app.Activity;
import android.util.Log;

import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLProcedureInvocationData;
import com.worklight.wlclient.api.WLRequestOptions;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.challengehandler.ChallengeHandler;

import org.json.JSONException;


public class TelcoChallengeHandler extends ChallengeHandler {
    private static final String DEMO_USERNAME = "user1";
    private static final String DEMO_PASSWORD = "password1";
    private static final String TELCO_AUTH_REALM = "AdapterAuthenticationRealm";
    private static final String TELCO_AUTH_ADAPTER = "AuthenticationAdapter";
    private static final String TELCO_AUTH_METHOD = "submitAuthentication";
    private WLResponse cachedResponse;

    public TelcoChallengeHandler() {
        super(TELCO_AUTH_REALM);
    }

    @Override
    public void onFailure(WLFailResponse response) {
        submitFailure(response);
    }

    @Override
    public void onSuccess(WLResponse response) {
        submitSuccess(response);
    }

    @Override
    public boolean isCustomResponse(WLResponse response) {
        try {
            if (response != null &&
                    response.getResponseJSON() != null &&
                !response.getResponseJSON().isNull("authRequired") &&
                response.getResponseJSON().getBoolean("authRequired")) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void handleChallenge(WLResponse response) {
        cachedResponse = response;
        /* This is where a developer would traditionally have code to spawn a login activity */
        Log.i("TEST", "HANDLE CHALLENGE EXECUTED");
        submitLogin(Activity.RESULT_OK, DEMO_USERNAME, DEMO_PASSWORD, false);
    }

    private void submitLogin(int resultCode, String userName, String password, boolean back) {
        if (resultCode != Activity.RESULT_OK || back) {
            submitFailure(cachedResponse);
        } else {
            Object[] parameters = new Object[]{userName, password};
            WLProcedureInvocationData invocationData = new WLProcedureInvocationData
                    (TELCO_AUTH_ADAPTER, TELCO_AUTH_METHOD);
            invocationData.setParameters(parameters);
            WLRequestOptions options = new WLRequestOptions();

            options.setTimeout(30000);
            submitAdapterAuthentication(invocationData, options);

            Log.i("TEST", "SUBMIT LOGIN");
        }
    }

}
