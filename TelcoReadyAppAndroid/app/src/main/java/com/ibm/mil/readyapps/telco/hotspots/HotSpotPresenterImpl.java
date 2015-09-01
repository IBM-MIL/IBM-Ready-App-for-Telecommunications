/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.hotspots;

import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class HotSpotPresenterImpl implements HotSpotPresenter {
    private final HotSpotView view;
    private final HotSpotModel model;
    private List<HotSpot> hotSpots;

    public HotSpotPresenterImpl(HotSpotView view, HotSpotModel model) {
        this.view = view;
        this.model = model;
    }

    @Override public Observable<HotSpot> getOnlineHotSpots(Geocoder geocoder, Location location) {
        return model.getHotSpots(location)
                .timeout(5, TimeUnit.SECONDS)
                .compose(new HotSpotTransformer(geocoder, location))
                .compose(cacheHotSpots());
    }

    @Override
    public Observable<HotSpot> getOfflineHotSpots(String json, Geocoder geocoder,
                                                  final Location location) {
        return Observable.just(json)
                .map(new Func1<String, List<HotSpot>>() {
                    @Override public List<HotSpot> call(String s) {
                        return new Gson().fromJson(s, new TypeToken<List<HotSpot>>() {
                        }.getType());
                    }
                })
                .flatMap(new Func1<List<HotSpot>, Observable<HotSpot>>() {
                    @Override public Observable<HotSpot> call(List<HotSpot> hotSpots) {
                        return model.getOfflineHotSpotLocations(location, hotSpots);
                    }
                })
                .compose(new HotSpotTransformer(geocoder, location))
                .compose(cacheHotSpots());
    }

    private Observable.Transformer<HotSpot, HotSpot> cacheHotSpots() {
        return new Observable.Transformer<HotSpot, HotSpot>() {
            @Override public Observable<HotSpot> call(Observable<HotSpot> observable) {
                return observable.toList()
                        .doOnNext(new Action1<List<HotSpot>>() {
                            @Override public void call(List<HotSpot> hotSpots) {
                                HotSpotPresenterImpl.this.hotSpots = hotSpots;
                            }
                        })
                        .flatMap(new Func1<List<HotSpot>, Observable<HotSpot>>() {
                            @Override public Observable<HotSpot> call(List<HotSpot> hotSpots) {
                                return Observable.from(hotSpots);
                            }
                        });
            }
        };
    }

    @Override public void watchMarkerClick(Observable<LatLng> markerObservable) {
        markerObservable.subscribe(new Action1<LatLng>() {
            @Override public void call(LatLng latLng) {
                scanHotSpots(latLng).subscribe(new Action1<HotSpot>() {
                    @Override public void call(HotSpot hotSpot) {
                        view.showHotSpotDetails(hotSpot);
                    }
                });
            }
        });
    }

    private Observable<HotSpot> scanHotSpots(final LatLng latLng) {
        return Observable.from(hotSpots)
                .filter(new Func1<HotSpot, Boolean>() {
                    @Override public Boolean call(HotSpot hotSpot) {
                        return hotSpot.getLatitude() == latLng.latitude &&
                                hotSpot.getLongitude() == latLng.longitude;
                    }
                }).first();
    }

}
