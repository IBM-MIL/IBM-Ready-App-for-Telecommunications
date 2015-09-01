/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.onboarding.appintrolib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

class ResourceUtils {
    @Nullable
    @SuppressWarnings("deprecation")
    static Drawable getDrawable(@NonNull Context context, @DrawableRes int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return context.getDrawable(drawableId);

        //noinspection deprecation
        return context.getResources().getDrawable(drawableId);
    }
}
