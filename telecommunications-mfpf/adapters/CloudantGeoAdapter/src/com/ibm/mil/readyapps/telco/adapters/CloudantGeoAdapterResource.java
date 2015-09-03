/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.mil.readyapps.telco.adapters;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.wink.common.http.HttpStatus;

import com.google.gson.Gson;
import com.ibm.mil.cloudant.geospatial.GeoJsonPoint;
import com.ibm.mil.cloudant.geospatial.GeoRadiusURI;
import com.ibm.mil.readyapps.telco.models.WifiHotspotFlat;
import com.ibm.mil.utils.WifiHotspotUtils;

@Path("/users")
public class CloudantGeoAdapterResource {
	
	private static final GeoJsonPoint DEMO_LOCATION = new GeoJsonPoint(10.1, 10.2);

	/*
	 * For more info on JAX-RS see
	 * https://jsr311.java.net/nonav/releases/1.1/index.html
	 */

	// Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(CloudantGeoAdapterResource.class
			.getName());

	/*
	 * Path for method:
	 * "<server address>/TelcoReadyAppMFP/adapters/CloudantGeoAdapter/users/{user_id}/wifi"
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{user_id}/wifi")
	public Response getWifiLocations(@PathParam("user_id") String name,
			@QueryParam(value = "lat") double latitude,
			@QueryParam(value = "lon") double longitude) {
		
		 GeoJsonPoint userLocation = new GeoJsonPoint(latitude, longitude);

		try {
			HttpGet httpget = new HttpGet(GeoRadiusURI.build(DEMO_LOCATION));

			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse httpResponse = httpClient.execute(httpget);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK
					.getCode()) {
				String jsonString = EntityUtils.toString(httpResponse
						.getEntity());

				List<WifiHotspotFlat> hotspots = WifiHotspotUtils.parseAndOffsetHotspots(jsonString, userLocation);

				return Response.ok(new Gson().toJson(hotspots), MediaType.APPLICATION_JSON)
						.build();
			}

			httpClient.close();
			return Response.serverError().entity(httpResponse.getStatusLine())
					.build();
		} catch (URISyntaxException | ParseException | IOException e) {
			e.printStackTrace();
			return Response.serverError().entity("Error").build();

		}
	}



}