package com.ibm.mil.readyapps.telco.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/** Collection of general purpose utility functions */
public final class Utils {

    private Utils() {
        throw new AssertionError(Utils.class.getName() + " is non-instantiable");
    }

    /** Determines if the device has an active network connection */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

}
