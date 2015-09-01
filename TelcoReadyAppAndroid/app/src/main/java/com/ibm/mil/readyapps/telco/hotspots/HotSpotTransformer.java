package com.ibm.mil.readyapps.telco.hotspots;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.ibm.mil.readyapps.telco.utils.MapUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/** An Observable Transformer for common HotSpot operations. To use, pass in an instance of this
 * class to Observable.compose(), where the Observable emits values of type HotSpot.
 */
class HotSpotTransformer implements Observable.Transformer<HotSpot, HotSpot> {
    private final Geocoder geocoder;
    private final Location userLocation;

    public HotSpotTransformer(Geocoder geocoder, Location userLocation) {
        this.geocoder = geocoder;
        this.userLocation = userLocation;
    }

    private static void setLatLngAddress(HotSpot hotSpot) {
        hotSpot.setAddressLine1(Double.toString(hotSpot.getLatitude()));
        hotSpot.setAddressLine2(Double.toString(hotSpot.getLongitude()));
    }

    private static Func2<HotSpot, HotSpot, Integer> sortByDistance() {
        return new Func2<HotSpot, HotSpot, Integer>() {
            @Override public Integer call(HotSpot hotSpot, HotSpot hotSpot2) {
                int distance1 = (int) hotSpot.getDistanceAway();
                int distance2 = (int) hotSpot2.getDistanceAway();
                return distance1 - distance2;
            }
        };
    }

    @Override public Observable<HotSpot> call(Observable<HotSpot> hotSpotObservable) {
        return hotSpotObservable.map(new Func1<HotSpot, HotSpot>() {
            @Override public HotSpot call(HotSpot hotSpot) {
                return calculateDistance(hotSpot);
            }
        }).map(new Func1<HotSpot, HotSpot>() {
            @Override public HotSpot call(HotSpot hotSpot) {
                return obtainAddress(hotSpot);
            }
        }).toSortedList(sortByDistance()).flatMap(new Func1<List<HotSpot>, Observable<HotSpot>>() {
            @Override public Observable<HotSpot> call(List<HotSpot> hotSpots) {
                return Observable.from(hotSpots);
            }
        });
    }

    private HotSpot calculateDistance(HotSpot hotSpot) {
        Location hotSpotLocation = MapUtils.convertLatLng(hotSpot.getLatitude(),
                hotSpot.getLongitude());
        hotSpot.setDistanceAway(MapUtils.distanceInKilometers(userLocation, hotSpotLocation));
        return hotSpot;
    }

    private HotSpot obtainAddress(HotSpot hotSpot) {
        if (geocoder != null) {
            try {
                List<Address> addresses = geocoder.getFromLocation(hotSpot.getLatitude(),
                        hotSpot.getLongitude(), 1);

                Address address = addresses.get(0);
                hotSpot.setAddressLine1(address.getAddressLine(0));
                hotSpot.setAddressLine2(address.getAddressLine(1));
            } catch (Exception e) {
                setLatLngAddress(hotSpot);
            }
        } else {
            setLatLngAddress(hotSpot);
        }

        return hotSpot;
    }

}
