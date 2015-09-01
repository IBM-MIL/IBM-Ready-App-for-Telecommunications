package com.ibm.mil.readyapps.telco.analytics;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation <2015>.
 *
 * GestureListener captures all gestures that a user performs in the app and allows analytics
 * platforms to record the different gestures.
 */
public class GestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String TAG = "GESTURE";

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(TAG, "onDown");
        MILAnalyticsReporter.gestureLogger("onDown", event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d(TAG, "onFling");
        MILAnalyticsReporter.gestureLogger("onFling", event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(TAG, "onLongPress");
        MILAnalyticsReporter.gestureLogger("onLongPress", event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        Log.d(TAG, "onScroll");
        MILAnalyticsReporter.gestureLogger("onScroll", e1.toString() + e2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(TAG, "onLongPress");
        MILAnalyticsReporter.gestureLogger("onShowPress", event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(TAG, "onSingleTapUp");
        MILAnalyticsReporter.gestureLogger("onSingleTapUp", event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(TAG, "onDoubleTap");
        MILAnalyticsReporter.gestureLogger("onDoubleTap", event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(TAG, "onDoubleTapEvent");
        MILAnalyticsReporter.gestureLogger("onDoubleTapEvent", event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d(TAG, "onSingleTapConfirmed");
        MILAnalyticsReporter.gestureLogger("onSingleTapConfirmed", event.toString());
        return true;
    }


}
