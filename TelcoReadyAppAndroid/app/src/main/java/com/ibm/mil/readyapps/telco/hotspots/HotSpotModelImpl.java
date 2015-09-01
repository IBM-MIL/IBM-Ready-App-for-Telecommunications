/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.hotspots;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.ibm.mil.cafejava.CafeJava;
import com.ibm.mil.cafejava.JavaProcedureInvoker;
import com.ibm.mil.readyapps.telco.utils.MapUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class HotSpotModelImpl implements HotSpotModel {
    private static final int HOTSPOT_RADIUS = 10000;

    @Override
    public Observable<HotSpot> getHotSpots(Location location) {
        TypeToken<List<HotSpot>> hotSpotsToken = new TypeToken<List<HotSpot>>() {
        };
        return CafeJava.invokeProcedure(new JavaProcedureInvoker
                .Builder("CloudantGeoAdapter", "users/{user_id}/wifi")
                .pathParam("user_id", "user1")
                .queryParam("lat", Double.toString(location.getLatitude()))
                .queryParam("lon", Double.toString(location.getLongitude()))
                .build())
                .compose(CafeJava.serializeTo(hotSpotsToken))
                .flatMap(new Func1<List<HotSpot>, Observable<HotSpot>>() {
                    @Override public Observable<HotSpot> call(List<HotSpot> hotSpots) {
                        return Observable.from(hotSpots);
                    }
                });
    }

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
