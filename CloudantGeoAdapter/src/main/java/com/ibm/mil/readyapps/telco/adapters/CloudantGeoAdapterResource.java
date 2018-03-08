/*
/ *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.mil.readyapps.telco.adapters;


import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.wink.common.http.HttpStatus;
import com.ibm.mil.cloudant.geospatial.GeoJsonPoint;
import com.ibm.mil.readyapps.telco.models.*;
import com.ibm.mil.utils.*;
import com.ibm.mfp.adapter.api.ConfigurationAPI;
import com.ibm.mfp.adapter.api.OAuthSecurity;
import com.google.gson.Gson;



@Path("/users")
public class CloudantGeoAdapterResource {

	
	private static final GeoJsonPoint DEMO_LOCATION = new GeoJsonPoint(10.1, 10.2);
	private static final String DEFAULT_SCHEME = "https";
	private static final String RADIUS_PARAM = "radius";
	private static final String LAT_PARAM = "lat";
	private static final String LONG_PARAM = "lon";
	private static final String INCLUDE_DOCS_PARAM = "include_docs"; 
	

	/*
	 * For more info on JAX-RS see
	 * https://jsr311.java.net/nonav/releases/1.1/index.html
	 */

	// Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(CloudantGeoAdapterResource.class.getName());
	@Context
	ConfigurationAPI configurationAPI;

	/*
	 * Path for method:
	 * "<server address>/TelcoReadyAppMFP/adapters/CloudantGeoAdapter/users/{user_id}/wifi"
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{user_id}/wifi")
	@OAuthSecurity(enabled=false)
	public Response getWifiLocations(@PathParam("user_id") String name,
			@QueryParam(value = "lat") double latitude,
			@QueryParam(value = "lon") double longitude) {
		
		 GeoJsonPoint userLocation = new GeoJsonPoint(latitude, longitude);
		 logger.info("Adapter 1");

		try {
			
			 StringBuilder sb = new StringBuilder();
			sb.append('/')
					.append(configurationAPI.getPropertyValue("CLOUDANT_DB_NAME"))
					.append("/_design/")
					.append(Constants.GEO_VIEW)
					.append("/_geo/")
					.append(Constants.GEO_INDEX);
			String geoPath = sb.toString();
			 
			 URI  uri = new URIBuilder()
					.setUserInfo(
							configurationAPI.getPropertyValue("CLOUDANT_USERNAME"),						
							configurationAPI.getPropertyValue("CLOUDANT_PASSWORD"))			
					.setScheme(DEFAULT_SCHEME)
					.setHost(configurationAPI.getPropertyValue("CLOUDANT_ACCOUNT"))
					.setPath(geoPath)
					.setParameter(RADIUS_PARAM,
							Integer.toString(Constants.GEO_QUERY_RADIUS))
					.setParameter(LAT_PARAM,
							Double.toString(DEMO_LOCATION.getLatitude()))
					.setParameter(LONG_PARAM,
							Double.toString(DEMO_LOCATION.getLongitude()))
					.setParameter(INCLUDE_DOCS_PARAM, "true").build(); 
			
		//	HttpGet httpget = new HttpGet(GeoRadiusURI.build(DEMO_LOCATION));	
			
			HttpGet httpget = new HttpGet(uri);

			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			
			HttpResponse httpResponse = httpClient.execute(httpget);
			

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK.getCode()) {
				String jsonString = EntityUtils.toString(httpResponse
						.getEntity());
				

				List<WifiHotspotFlat> hotspots = WifiHotspotUtils.parseAndOffsetHotspots(jsonString, userLocation);

				
				return Response.ok(new Gson().toJson(hotspots), MediaType.APPLICATION_JSON)
						.build();
				
			}

			httpClient.close();
			return Response.serverError().entity(httpResponse.getStatusLine())
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Adapter 6");
			return Response.serverError().entity("Error").build();

		}
	}



}