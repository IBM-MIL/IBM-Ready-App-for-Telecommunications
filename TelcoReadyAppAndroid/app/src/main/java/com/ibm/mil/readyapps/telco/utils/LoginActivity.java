package com.ibm.mil.readyapps.telco.utils;
/**
* Copyright 2016 IBM Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.activities.LaunchActivity;

public class LoginActivity extends AppCompatActivity {



    private EditText usernameInput, passwordInput;
    private TextView errorMsgDisplay, remainingAttemptsDisplay;

    private BroadcastReceiver loginErrorReceiver, loginRequiredReceiver, loginSuccessReceiver;

    private LoginActivity _this;

    private final String DEBUG_NAME = "LoginActivity";
    public static JSONObject credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _this = this;



        //Initialize the UI elements
        usernameInput = (EditText)findViewById(R.id.usernameInput);
        passwordInput = (EditText)findViewById(R.id.passwordInput);
        errorMsgDisplay = (TextView)findViewById(R.id.errorMsg);
        remainingAttemptsDisplay = (TextView)findViewById(R.id.remainingAttempts);
        Button loginButton = (Button) findViewById(R.id.login);

        //Login Button behavior
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usernameInput.getText().toString().isEmpty() || passwordInput.getText().toString().isEmpty()){
                    alertError("Username and password are required");
                }
                else{
                     credentials = new JSONObject();
                    try {
                        credentials.put("username",usernameInput.getText().toString());
                        credentials.put("password",passwordInput.getText().toString());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.setAction(Constants.ACTION_LOGIN);
                    intent.putExtra("credentials",credentials.toString());
                    LocalBroadcastManager.getInstance(_this).sendBroadcast(intent);
                }
            }
        });

        //Login error receiver
        loginErrorReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(DEBUG_NAME, "loginErrorReceiver");
                errorMsgDisplay.setText("");
                remainingAttemptsDisplay.setText("");
                alertError(intent.getStringExtra("errorMsg"));
            }
        };

        //Login required
        loginRequiredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                Log.d(DEBUG_NAME, "loginRequiredReceiver");
                Runnable run = new Runnable() {
                    public void run() {
                        //Set error message:
                        errorMsgDisplay.setText(intent.getStringExtra("errorMsg"));

                        //Display remaining attempts
                        if(intent.getIntExtra("remainingAttempts",-1) > -1) {
                            remainingAttemptsDisplay.setText(getString(R.string.remaining_attempts, intent.getIntExtra("remainingAttempts",-1)));
                        }
                    }
                };
                _this.runOnUiThread(run);


            }
        };

        //Login success
        loginSuccessReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(isTaskRoot()){
                    //First time, go to protected area
                    Intent openLogin = new Intent(_this, LaunchActivity.class);
                    _this.startActivity(openLogin);
                } else{
                    //Other times, go "back" to wherever you came from
                    finish();
                }

            }
        };


    }

    @Override
    protected void onStart() {
        Log.d(DEBUG_NAME, "onStart");
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(loginRequiredReceiver, new IntentFilter(Constants.ACTION_LOGIN_REQUIRED));
        LocalBroadcastManager.getInstance(this).registerReceiver(loginErrorReceiver, new IntentFilter(Constants.ACTION_LOGIN_FAILURE));
        LocalBroadcastManager.getInstance(this).registerReceiver(loginSuccessReceiver, new IntentFilter(Constants.ACTION_LOGIN_SUCCESS));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(DEBUG_NAME, "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginErrorReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginRequiredReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginSuccessReceiver);
    }

    public void alertError(final String msg) {

        Runnable run = new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(_this);
                builder.setMessage(msg)
                        .setTitle("Error");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
        _this.runOnUiThread(run);
    }


}
