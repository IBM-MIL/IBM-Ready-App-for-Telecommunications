/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.hotspots;

import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import rx.Observable;

public interface HotSpotPresenter {
    /**
     * Generates a stream of hotspots for the given location, using the Geocoder for address
     * lookups
     */
    Observable<HotSpot> getOnlineHotSpots(Geocoder geocoder, Location location);

    /**
     * Generates a stream of HotSpots for a given location, without the need for an internet
     * connection.
     */
    Observable<HotSpot> getOfflineHotSpots(String json, Geocoder geocoder, Location location);

    /**
     * Listens for marker clicks on Google Maps, indicating a new location has been chosen by the
     * user.
     */
    void watchMarkerClick(Observable<LatLng> markerObservable);
}
