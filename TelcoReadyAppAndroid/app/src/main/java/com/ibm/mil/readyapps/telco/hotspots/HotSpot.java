package com.ibm.mil.readyapps.telco.hotspots;

import com.google.gson.Gson;

public class HotSpot {
    private double latitude;
    private double longitude;
    private String name;
    private boolean isVerified;
    private double distanceAway;
    private String addressLine1;
    private String addressLine2;
    private int downloadSpeed;
    private boolean signInRequired;
    private int connections;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public double getDistanceAway() {
        return distanceAway;
    }

    public void setDistanceAway(double distanceAway) {
        this.distanceAway = distanceAway;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public int getDownloadSpeed() {
        return downloadSpeed;
    }

    public boolean getSignInRequired() {
        return signInRequired;
    }

    public int getConnections() {
        return connections;
    }

    @Override public String toString() {
        return new Gson().toJson(this);
    }
}
