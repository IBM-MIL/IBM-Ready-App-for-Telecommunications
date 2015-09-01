package com.ibm.mil.readyapps.telco.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

/**
 * Helper class for showing directions to a given location.
 */
public final class MapUtils {

    /**
     * Set up the URI for a maps based intent that includes lat/long
     * for both starting location and destination address.
     *
     * @param ctx the context needed to launch the activity with intent
     * @param start the starting location
     * @param dest the destination location
     */
    public static void showDirections(Context ctx, Location start, Location dest) {
        String uri = String.format(ctx.getResources().getConfiguration().locale, "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",
                start.getLatitude(), start.getLongitude(), dest.getLatitude(), dest.getLongitude());
        startIntent(ctx, uri);
    }

    /**
     * Helper method for converting a LatLng type to Location type
     *
     * @param location the LatLng object to convert
     * @return the location object after transformation
     */
    public static Location convertLatLng(LatLng location) {
        return convertLatLng(location.latitude, location.longitude);
    }

    /**
     * Convert latitude and longitude to a Location object
     *
     * @param latitude the latitude of location
     * @param longitude the longitude of location
     * @return the Location object created from lat/lng coords
     */
    public static Location convertLatLng(double latitude, double longitude) {
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    /**
     * Get the distance between two points in kilometers
     *
     * @param start the starting location
     * @param end the destination location
     * @return the distance between starting and destination location
     */
    public static double distanceInKilometers(Location start, Location end) {
        return start.distanceTo(end) / 1000;
    }

    /**
     * Generate and return random coordinate near origin location.
     *
     * @param origin the location to generate coordinate near
     * @param radius the max radius that the generated coordinate can be from origin
     * @return the lat/lng of nearby coordinate
     */
    public static LatLng getNearbyCoordinate(LatLng origin, int radius) {
        Random random = new Random();

        // convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // adjust the x-coordinate for the shrinking of the east-west distances
        double newX = x / Math.cos(origin.latitude);

        double foundLatitude = y + origin.latitude;
        double foundLongitude = newX + origin.longitude;

        return new LatLng(foundLatitude, foundLongitude);
    }

    /**
     * Start the maps intent which should launch the Maps app
     *
     * @param context the context necessary to call startActivity
     * @param uri the uri to parse for locations to display after intent launched
     */
    private static void startIntent(Context context, String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }

    /**
     * This is only a helper class with all static methods and should not be instantiable
     */
    private MapUtils() {
        throw new AssertionError(MapUtils.class.getName() + " is non-instantiable");
    }

}
