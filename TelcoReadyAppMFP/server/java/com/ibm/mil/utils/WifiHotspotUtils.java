package com.ibm.mil.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ibm.mil.cloudant.geospatial.GeoJsonPoint;
import com.ibm.mil.readyapps.telco.models.WifiHotspot;
import com.ibm.mil.readyapps.telco.models.WifiHotspotFlat;

public final class WifiHotspotUtils {
	
	private WifiHotspotUtils() {
		throw new AssertionError("Utilities is non-instantiable");
	}

	public static List<WifiHotspotFlat> parseAndOffsetHotspots(String stringResponse, GeoJsonPoint userLocation) {
		List<WifiHotspotFlat> hotspots = new ArrayList<WifiHotspotFlat>();

		JsonArray array = new JsonParser().parse(stringResponse)
				.getAsJsonObject().getAsJsonArray("rows");

		Gson gson = new Gson();
		WifiHotspot temp;

		if (array.size() > 0) {
			for (JsonElement elem : array) {
				JsonElement e = elem.getAsJsonObject().get("doc");
				temp = gson.fromJson(e, WifiHotspot.class);
				
				temp.setGeometry(WifiHotspotUtils.randomLocationOffset(
						userLocation.getLongitude(), 
						userLocation.getLatitude(),
						Constants.GEO_RETURN_RADIUS));
				
				hotspots.add(temp.flatten());
			}
		}

		return hotspots;
	}

	private static GeoJsonPoint randomLocationOffset(double x0, double y0,
			int radius) {
		Random random = new Random();

		// Convert radius from meters to degrees
		double radiusInDegrees = radius / 111000f;

		double u = random.nextDouble();
		double v = random.nextDouble();
		double w = radiusInDegrees * Math.sqrt(u);
		double t = 2 * Math.PI * v;
		double x = w * Math.cos(t);
		double y = w * Math.sin(t);

		// Adjust the x-coordinate for the shrinking of the east-west distances
		double new_x = x / Math.cos(y0);

		double foundLongitude = new_x + x0;
		double foundLatitude = y + y0;
		// System.out.println("Longitude: " + foundLongitude + "  Latitude: " +
		// foundLatitude );

		return new GeoJsonPoint(foundLatitude, foundLongitude);
	}

}
