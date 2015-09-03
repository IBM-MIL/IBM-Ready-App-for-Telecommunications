package com.ibm.mil.cloudant.geospatial;

/**
 * GeoJsonPoint is a mclassed that is used to deserialize the GeoJson returned from CloudantGeo.
 * 
 * In the payload from a CloudantGeo query there is a default attribute called 'coordinates' and type.
 * 
 * @author tannerpreiss
 */
public class GeoJsonPoint {
	//The Geo JSON formatting requires an array called 'coordinates' and so we must 
	//set the latitude and longitude to indices of this array.
	private static final int LAT_IDX = 0;
	private static final int LON_IDX = 1;
	private static final int TOTAL_COORDINATES = 2;
	
	private final double[] coordinates = new double[TOTAL_COORDINATES];
	private String type;
	
	public GeoJsonPoint(double latitude, double longitude) {
		coordinates[LAT_IDX] = latitude;
		coordinates[LON_IDX] = longitude;
	}
	
	/**
	 * getLatitude() returns the latitude value for the GeoJsonPoint
	 * @return latitude the latitude for the GeoJsonPoint.
	 */
	public double getLatitude() {
		return coordinates[LAT_IDX];
	}
	
	/**
	 * getLongitude() returns the longitude value for the GeoJsonPoint
	 * @return longitude the longitude for the GeoJsonPoint.
	 */
	public double getLongitude() {
		return coordinates[LON_IDX];
	}

	/**
	 * getType() returns the CloudantGeo geoJson type. 
	 * In this case it is always a 'Point'.
	 * @return the type of geoJSON object which is a 'Point' in this case. 
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
