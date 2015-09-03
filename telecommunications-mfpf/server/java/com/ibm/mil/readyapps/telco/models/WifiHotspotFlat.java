package com.ibm.mil.readyapps.telco.models;

/**
 * Flattened pojo for Gson serialization to represent a wifi hotspot.
 *
 * NOTE: This class is only used as a serializable class by Gson, and this is why
 * there are no getters or setters because Gson creates JSON from the fields of
 * an object.
 */
public class WifiHotspotFlat {
	public double latitude;
	public double longitude;
	public String name;
	public boolean isVerified;
	public double downloadSpeed;
	public boolean signInRequired;
	public int connections;
	
	public WifiHotspotFlat(WifiHotspot origHotspot) {
		latitude = origHotspot.getGeometry().getLatitude();
		longitude = origHotspot.getGeometry().getLongitude();
		
		name = origHotspot.getProperties().getName();
		isVerified = origHotspot.getProperties().isVerified();
		downloadSpeed = origHotspot.getProperties().getDownloadSpeed();
		signInRequired = origHotspot.getProperties().isSignInRequired();
		connections = origHotspot.getProperties().getConnections();
	}
	
}
