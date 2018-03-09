/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.hotspots;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;
import android.util.Log;

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
import com.worklight.wlclient.api.WLResourceRequest;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;


import static com.ibm.mil.readyapps.telco.utils.LoginActivity.credentials;

public class HotSpotActivity extends AppCompatActivity implements HotSpotView, OnMapReadyCallback,LocationListener {
    private static final LatLng DEFAULT_LOCATION = new LatLng(35.913436, -78.858730);
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
    private Context mcontext;
    Location location,userLocation;
    final List<HotSpot> hotSpots = new ArrayList<HotSpot>();


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
        mcontext = HotSpotActivity.this;
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
        try {
            map.setMyLocationEnabled(true);
        }
        catch(SecurityException e) {

        }
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().setCompassEnabled(true);

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
                //  markerPublisher.onNext(marker.getPosition());

                    for (HotSpot newHotSpot : hotSpots) {

                    if (marker.getPosition().latitude == newHotSpot.getLatitude() &&
                            marker.getPosition().longitude == newHotSpot.getLongitude())
                    {

                        showHotSpotDetails(newHotSpot);

                        // reset marker icon to dot
                        if (currentMarker != null) {
                            currentMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.dot));
                            currentMarker.setAnchor(0.5f, 0.5f);
                        }

                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
                        marker.setAnchor(0.5f, 1.0f);
                        currentMarker = marker;
                        break;
                    }

                }

                return false;
            }
        });


        userLocation = getLocation();
    }

    private void useOnlineMode(final Location userLocation) {
        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        try {
            String userid = credentials.getString("username");
            URI uri = new URI("adapters/CloudantGeoAdapter/users/"+userid+"/wifi");
            WLResourceRequest request = new WLResourceRequest(uri, WLResourceRequest.GET);

            request.setQueryParameter("lat", Double.toString(userLocation.getLatitude()));
            request.setQueryParameter("lon", Double.toString(userLocation.getLongitude()));

            request.send(new WLResponseListener(){
                public void onSuccess(WLResponse response) {
                    try{
                        String responseJson = response.getResponseText();
                        JSONArray jsonArray = new JSONArray(responseJson);
                        HotSpotTransformer transformer = new HotSpotTransformer(geocoder, userLocation);

                        // Convert the adapter JSON response to HotSpot

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            HotSpot hotSpot = new HotSpot();
                            hotSpot.setLatitude(jsonObject.getDouble("latitude"));
                            hotSpot.setLongitude(jsonObject.getDouble("longitude"));
                            transformer.obtainAddress(hotSpot);
                            transformer.calculateDistance(hotSpot);
                            hotSpot.setDownloadSpeed(jsonObject.getInt("downloadSpeed"));
                            hotSpot.setConnections(jsonObject.getInt("connections"));
                            hotSpot.setSignInRequired(jsonObject.getBoolean("signInRequired"));
                            hotSpot.setVerified(jsonObject.getBoolean("isVerified"));
                            hotSpots.add(hotSpot);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                displayHotSpots(hotSpots);
                            }
                        });


                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    Log.i("Adapter Success", response.getResponseText());
                }
                public void onFailure(WLFailResponse response) {
                    Log.i("Adapter Failure", response.getErrorMsg());
                    // display alert message
                    alertMessage("Could not retrieve data from HotSpots DB, working in offline mode");
                    // invoke offlinemode
                    useOfflineMode(userLocation,geocoder);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }



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

    private void initUserMarker() {



        LatLng userPosition = DEFAULT_LOCATION; // default if no user location exists
        if (userLocation != null) {
            userPosition = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        }


        userMarker = map.addMarker(new MarkerOptions()
                .position(userPosition)
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.person)));

        userLocation = MapUtils.convertLatLng(userMarker.getPosition());
        if (Utils.isConnected(this)) {
            useOnlineMode(userLocation);
        } else {
            alertMessage("No network connection, working in offline mode");
            useOfflineMode(userLocation, null);
        }


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
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 5.0f));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10.0f));
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

    //new code



    public Location getLocation() {
        try {

            LocationManager locationManager = (LocationManager) mcontext.getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (ActivityCompat.checkSelfPermission(mcontext,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }

                if (isNetworkEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    }
                }
              userLocation=  location;
                initUserMarker();
            }
            else
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getLocation();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(HotSpotActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderDisabled(String provider) {

    }

    public  void alertMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(HotSpotActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage(message);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }


}
