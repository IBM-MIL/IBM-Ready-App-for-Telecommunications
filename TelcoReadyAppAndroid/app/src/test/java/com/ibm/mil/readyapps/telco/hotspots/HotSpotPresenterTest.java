package com.ibm.mil.readyapps.telco.hotspots;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.ibm.mil.readyapps.telco.utils.MapUtils;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rx.Observable;
import rx.functions.Action1;

public class HotSpotPresenterTest {
    HotSpotViewMock viewMock;
    HotSpotPresenter presenter;
    HotSpot hotSpot;

    @Before
    public void setUp() {
        viewMock = new HotSpotViewMock();
        presenter = new HotSpotPresenterImpl(viewMock, new HotSpotModelMock());
    }

    @Test
    public void testGetOfflineHotSpots() {
        presenter.getOfflineHotSpots(getMockJson(), null, getMockLocation())
                .first()
                .subscribe(new Action1<HotSpot>() {
                    @Override public void call(HotSpot hotSpot) {
                        HotSpotPresenterTest.this.hotSpot = hotSpot;
                    }
                });

        Assert.assertNotNull(hotSpot);
        Assert.assertEquals("HotSpot 0", hotSpot.getName());
    }

    @Test
    public void testWatchMarkerClick() {
        presenter.getOfflineHotSpots(getMockJson(), null, getMockLocation())
                .subscribe(new Action1<HotSpot>() {
                    @Override public void call(HotSpot hotSpot) {
                        HotSpotPresenterTest.this.hotSpot = hotSpot;
                    }
                });

        hotSpot.setLatitude(1.0);
        hotSpot.setLongitude(1.0);

        presenter.watchMarkerClick(Observable.just(new LatLng(1.0, 1.0)));
        HotSpot hotSpot = viewMock.getHotSpot();

        Assert.assertNotNull(hotSpot);
        Assert.assertEquals(1.0, hotSpot.getLatitude());
        Assert.assertEquals(1.0, hotSpot.getLongitude());
    }

    @After
    public void tearDown() {
        hotSpot = null;
    }

    private String getMockJson() {
        HotSpot[] hotSpots = new HotSpot[10];
        for (int i = 0, size = hotSpots.length; i < size; i++) {
            HotSpot hotSpot = new HotSpot();
            hotSpot.setName("HotSpot " + i);
            hotSpots[i] = hotSpot;
        }

        return new Gson().toJson(hotSpots);
    }

    private Location getMockLocation() {
        return MapUtils.convertLatLng(30.398974, 97.712885);
    }

}
