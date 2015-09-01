/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.hotspots;

import android.location.Location;

import java.util.List;

import rx.Observable;

public interface HotSpotModel {
    /** Generates a stream of HotSpots based on the location passed in */
    Observable<HotSpot> getHotSpots(Location location);

    /** Annotates an existing list of HotSpots based on the given location without requiring
     * an internet connection. The newly annotated list is generated as a stream. */
    Observable<HotSpot> getOfflineHotSpotLocations(Location location, List<HotSpot> hotSpots);
}
