package com.ibm.mil.readyapps.telco.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ibm.mil.cloudant.geospatial.GeoJsonPoint;
import com.ibm.mil.readyapps.telco.adapters.CloudantGeoAdapterResource;
import com.ibm.mil.readyapps.telco.models.WifiHotspot;
import com.ibm.mil.readyapps.telco.models.WifiHotspotFlat;
import com.ibm.mil.utils.Constants;
import com.ibm.mil.utils.JsonDataReader;

public class CloudantGeoAdapterTest {

	private static final String DEMO_USER = "user1";
	private static final GeoJsonPoint DEMO_LOCATION = new GeoJsonPoint(10.1,
			10.2);
	private static final Gson gson = new Gson();

	/**
	 * getWifiLocationsTest() tests the getWifiLocations() method in CloudantGeoAdapter.
	 * 
	 * This test performs a query using the demo location also used in the CloudantGeoAdapter
	 * and asserts that the local json file matches the query results. This is for 
	 * regression testing and ensures that our database is not changing and that the 
	 * query is still working.
	 */
	@Test
	public void getWifiLocationsTest() {

		CloudantGeoAdapterResource cloudantAdapter = new CloudantGeoAdapterResource();

		Response httpResponse = cloudantAdapter.getWifiLocations(DEMO_USER,
				DEMO_LOCATION.getLatitude(), DEMO_LOCATION.getLongitude());

		assertEquals(Response.Status.OK.getStatusCode(),
				httpResponse.getStatus());

		TypeToken<List<WifiHotspotFlat>> hotspotFlatToken = new TypeToken<List<WifiHotspotFlat>>() {
		};
		List<WifiHotspotFlat> remoteHotspots = gson.fromJson(httpResponse
				.getEntity().toString(), hotspotFlatToken.getType());

		List<WifiHotspotFlat> localHotspots = this.getLocalHotspots();
		
		for (WifiHotspotFlat localHotspot : localHotspots) {
			for (WifiHotspotFlat remoteHotspot : remoteHotspots) {
				if (localHotspot.name.equals(remoteHotspot.name)) { 
					assertHotspotsEqual(localHotspot, remoteHotspot);
				}
			}
		}
	}
	
	private List<WifiHotspotFlat> getLocalHotspots() {
		TypeToken<List<WifiHotspot>> hotspotToken = new TypeToken<List<WifiHotspot>>() {
		};
		List<WifiHotspot> localHotspots = JsonDataReader.getCollection(
				hotspotToken, Constants.FILENAME_HOTSPOTS);
		List<WifiHotspotFlat> localHotspotsFlat = new ArrayList<WifiHotspotFlat>();
			
		for (WifiHotspot localHotspot : localHotspots) {
			localHotspotsFlat.add(localHotspot.flatten());
		}
		
		return localHotspotsFlat;
	}

	private void assertHotspotsEqual(WifiHotspotFlat h1, WifiHotspotFlat h2) {
		assertEquals(h1.connections, h2.connections);
		assertEquals(h1.downloadSpeed, h2.downloadSpeed, 0.01);
		assertEquals(h1.isVerified, h2.isVerified);
		assertEquals(h1.name, h2.name);
		assertEquals(h1.signInRequired, h2.signInRequired);
	}
}
