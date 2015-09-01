/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.hotspots;

import android.graphics.Point;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.analytics.GestureListener;
import com.ibm.mil.readyapps.telco.utils.JsonUtils;
import com.ibm.mil.readyapps.telco.utils.MapUtils;
import com.ibm.mil.readyapps.telco.utils.RxUtils;
import com.ibm.mil.readyapps.telco.utils.Utils;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class HotSpotActivity extends AppCompatActivity implements HotSpotView, OnMapReadyCallback {
    private static final LatLng DEFAULT_LOCATION = new LatLng(30.398974, -97.712885);
    private static final DecimalFormat distanceFormatter = new DecimalFormat("0.0");
    private GestureDetectorCompat detector;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.nested_scroll_view) NestedScrollView nestedScrollView;
    @Bind(R.id.map_area) View mapArea;
    @Bind(R.id.map_touchevent_capture_view) View mapTouchEventCaptureView;
    @Bind(R.id.content_area) View contentArea;
    @Bind(R.id.progress_bar) CircleProgressBar progressBar;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.hotspot_name) TextView hotSpotName;
    @Bind(R.id.verification_status) TextView verificationStatus;
    @Bind(R.id.hotspot_distance) TextView hotspotDistance;
    @Bind(R.id.address_line1) TextView addressLine1;
    @Bind(R.id.address_line2) TextView addressLine2;
    @Bind(R.id.download_speed) TextView downloadSpeed;
    @Bind(R.id.sign_in_requirement) TextView signInRequirement;
    @Bind(R.id.connection_count) TextView connectionCount;
    private GoogleMap map;
    private HotSpot currentHotSpot;
    private Marker currentMarker;
    private Marker userMarker;
    private HotSpotPresenter presenter;
    private BitmapDescriptor activePin;
    private BitmapDescriptor defaultPin;

    private static SpannableString spanSuffix(String text, String suffix, Object... spans) {
        String fullText = text + " " + suffix;
        int startIndex = fullText.indexOf(suffix);
        int endIndex = fullText.length();

        return createSpan(fullText, startIndex, endIndex, spans);
    }

    private static SpannableString createSpan(String text, int start, int stop, Object... spans) {
        SpannableString spannableString = new SpannableString(text);
        for (Object span : spans) {
            spannableString.setSpan(span, start, stop, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_spot);
        ButterKnife.bind(this);
        detector = new GestureDetectorCompat(this, new GestureListener());
        presenter = new HotSpotPresenterImpl(this, new HotSpotModelImpl());

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressBar.setColorSchemeColors(R.color.dark_indigo);

        // make map area 1/3 of available screen size
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        mapArea.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                screenSize.y / 3));

        // prevent nested scroll view from scrolling up and down when gesture is performed on map
        mapTouchEventCaptureView.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_DOWN:
                        nestedScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        activePin = BitmapDescriptorFactory.fromResource(R.drawable.pin);
        defaultPin = BitmapDescriptorFactory.fromResource(R.drawable.dot);

        map.setMyLocationEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setCompassEnabled(false);

        // have presenter watch for marker clicks
        final PublishSubject<LatLng> markerPublisher = PublishSubject.create();
        presenter.watchMarkerClick(markerPublisher);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override public boolean onMarkerClick(Marker marker) {
                // define no behavior for clicking user marker
                if (marker.getPosition().equals(userMarker.getPosition())) {
                    return true;
                }

                // notify subscriber (presenter) that a new marker was selected
                // this will cause the presenter to fetch the detailed data for the hotspot
                markerPublisher.onNext(marker.getPosition());

                // reset marker icon to dot
                if (currentMarker != null) {
                    currentMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.dot));
                    currentMarker.setAnchor(0.5f, 0.5f);
                }

                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
                marker.setAnchor(0.5f, 1.0f);
                currentMarker = marker;



                return false;
            }
        });

        Location userLocation = initUserMarker();
        if (Utils.isConnected(this)) {
            useOnlineMode(userLocation);
        } else {
            useOfflineMode(userLocation, null);
        }
    }

    private void useOnlineMode(final Location userLocation) {
        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        presenter.getOnlineHotSpots(geocoder, userLocation)
                .compose(hotSpotTransformer())
                .subscribe(hotSpotOnNext(), new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        useOfflineMode(userLocation, geocoder);
                    }
                });
    }

    private void useOfflineMode(Location userLocation, Geocoder geocoder) {
        String json = JsonUtils.parseJsonFile(this, "offline_hotspots.json");
        presenter.getOfflineHotSpots(json, geocoder, userLocation)
                .compose(hotSpotTransformer())
                .subscribe(hotSpotOnNext());
    }

    private Observable.Transformer<HotSpot, List<HotSpot>> hotSpotTransformer() {
        return new Observable.Transformer<HotSpot, List<HotSpot>>() {
            @Override public Observable<List<HotSpot>> call(Observable<HotSpot> observable) {
                return observable.toList().compose(RxUtils.<List<HotSpot>>showBackgroundWork());
            }
        };
    }

    private Action1<List<HotSpot>> hotSpotOnNext() {
        return new Action1<List<HotSpot>>() {
            @Override public void call(List<HotSpot> hotSpots) {
                displayHotSpots(hotSpots);
            }
        };
    }

    private Location initUserMarker() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        LatLng userPosition = DEFAULT_LOCATION; // default if no user location exists
        if (userLocation != null) {
            userPosition = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        }

        userMarker = map.addMarker(new MarkerOptions()
                .position(userPosition)
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.person)));

        return MapUtils.convertLatLng(userMarker.getPosition());
    }

    private void displayHotSpots(List<HotSpot> hotSpots) {
        for (int i = 0, size = hotSpots.size(); i < size; i++) {
            HotSpot hotSpot = hotSpots.get(i);
            LatLng location = new LatLng(hotSpot.getLatitude(), hotSpot.getLongitude());

            Marker marker = map.addMarker(new MarkerOptions()
                    .position(location)
                    .anchor(0.5f, 0.5f)
                    .icon(defaultPin));

            // select first (nearest) hotspot
            if (i == 0) {
                currentMarker = marker;
                currentHotSpot = hotSpot;
                showHotSpotDetails(hotSpot);
                marker.setIcon(activePin);
                marker.setAnchor(0.5f, 1.0f);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10.0f));
            }
        }

        contentArea.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

    @Override public void showHotSpotDetails(HotSpot hotSpot) {
        currentHotSpot = hotSpot;

        hotSpotName.setText(hotSpot.getName());
        addressLine1.setText(hotSpot.getAddressLine1());
        addressLine2.setText(hotSpot.getAddressLine2());

        if (hotSpot.isVerified()) {
            verificationStatus.setText(R.string.verified_network);
        } else {
            verificationStatus.setText(R.string.unverified_network);
        }

        if (hotSpot.getSignInRequired()) {
            signInRequirement.setText(R.string.sign_in);
        } else {
            signInRequirement.setText(R.string.no_sign_in);
        }

        downloadSpeed.setText(spanSuffix(Integer.toString(hotSpot.getDownloadSpeed()),
                getString(R.string.internet_unit), new RelativeSizeSpan(0.75f)));

        connectionCount.setText(spanSuffix(Integer.toString(hotSpot.getConnections()),
                getString(R.string.connections), new RelativeSizeSpan(0.75f)));

        hotspotDistance.setText(spanSuffix(distanceFormatter.format(hotSpot.getDistanceAway()),
                getString(R.string.distance_away), new RelativeSizeSpan(0.66f),
                new ForegroundColorSpan(getResources().getColor(R.color.gray_ae))));
    }

    @OnClick(R.id.fab)
    public void getDirections(View view) {
        MapUtils.showDirections(this, MapUtils.convertLatLng(userMarker.getPosition()),
                MapUtils.convertLatLng(currentHotSpot.getLatitude(), currentHotSpot.getLongitude()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}
