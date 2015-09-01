package com.ibm.mil.readyapps.telco.models;

import com.ibm.mil.cloudant.geospatial.GeoJsonPoint;

public class WifiHotspot {
	GeoJsonPoint geometry;
	private WifiHotspotProperties properties;
	
	public GeoJsonPoint getGeometry() {
		return geometry;
	}
	public void setGeometry(GeoJsonPoint geometry) {
		this.geometry = geometry;
	}
	public WifiHotspotProperties getProperties() {
		return properties;
	}
	public void setProperties(WifiHotspotProperties properties) {
		this.properties = properties;
	}

	public WifiHotspotFlat flatten() {
		return new WifiHotspotFlat(this);
	}
}
