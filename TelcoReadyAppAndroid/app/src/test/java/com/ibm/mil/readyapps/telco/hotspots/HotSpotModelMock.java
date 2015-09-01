package com.ibm.mil.readyapps.telco.hotspots;

import android.location.Location;

import java.util.List;

import rx.Observable;

public class HotSpotModelMock implements HotSpotModel {
    @Override public Observable<HotSpot> getHotSpots(Location location) {
        return null;
    }

    @Override
    public Observable<HotSpot> getOfflineHotSpotLocations(Location location, List<HotSpot> hotSpots) {
        return Observable.from(hotSpots);
    }
}
