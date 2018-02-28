package com.ibm.mil.readyapps.telco.utils;

import android.app.Application;

import com.worklight.wlclient.api.WLClient;

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

public class PreemptiveLoginApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize the MobileFirst SDK. This needs to happen just once.
        WLClient.createInstance(this);
        //Initialize the challenge handler
        TelcoChallengeHandler.createAndRegister();
    }
}
