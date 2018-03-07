/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.hotspots;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.ibm.mil.readyapps.telco.utils.MapUtils;

import java.util.List;
import rx.Observable;


public class HotSpotModelImpl implements HotSpotModel {
    private static final int HOTSPOT_RADIUS = 10000;


    @Override
    public Observable<HotSpot> getOfflineHotSpotLocations(Location location,
                                                                    List<HotSpot> hotSpots) {
        for (HotSpot hotSpot : hotSpots) {
            LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
            LatLng position = MapUtils.getNearbyCoordinate(origin, HOTSPOT_RADIUS);
            hotSpot.setLatitude(position.latitude);
            hotSpot.setLongitude(position.longitude);
        }

        return Observable.from(hotSpots);
    }

}
