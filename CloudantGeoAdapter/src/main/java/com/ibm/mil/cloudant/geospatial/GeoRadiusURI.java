package com.ibm.mil.cloudant.geospatial;

import java.net.URI;
import java.net.URISyntaxException;


import org.apache.http.client.utils.URIBuilder;


import com.ibm.mil.utils.Constants;


/**
 * GeoRadiusURI is a utility class that builds a URI string for an HTTP GET
 * request to Cloudant Geospatial using cloudant credentials found in
 * /resources/app.properties and the location parameter passed in.
 * 
 * build() creates the following URI:
 * 
 * curl -u
 * "https://<cloudant-username>:<cloudant-password>@<cloudant-username>.cloudant.com
 * / <database-name>/_design/<design-doc>/_geo/<index-name>?lat=<latitude-value>
 * &lon=<longitude-value>&radius=<default-radius>"
 * 
 * @author tannerpreiss
 *
 */
public final class GeoRadiusURI {

	private static final String DEFAULT_SCHEME = "https";
	private static final String RADIUS_PARAM = "radius";
	private static final String LAT_PARAM = "lat";
	private static final String LONG_PARAM = "lon";
	private static final String INCLUDE_DOCS_PARAM = "include_docs";
	
	

	private GeoRadiusURI() {
		throw new AssertionError(GeoRadiusURI.class.getName()
				+ " is non-instantiable");
	}

	/**
	 * build() build() creates the following URI:
	 * 
	 * curl -u
	 * "https://<cloudant-username>:<cloudant-password>@<cloudant-username>.cloudant.com
	 * / <database-name>/_design/<design-doc>/_geo/<index-name>?lat=<latitude-
	 * value>&lon=<longitude-value>&radius=<default-radius>"
	 * 
	 * @param location the location to query around.
	 * @return URI the uri built from the location query.
	 * @throws URISyntaxException
	 */
	public static URI build(GeoJsonPoint location) throws URISyntaxException {
		
		

		URI uri = new URIBuilder()
				.setUserInfo(
						Constants.CLOUDANT_USERNAME,
						Constants.CLOUDANT_PASSWORD	)					
				.setScheme(DEFAULT_SCHEME)
				.setHost(
						Constants.CLOUDANT_ACCOUNT)
				.setPath(GeoRadiusURI.buildGeoPath())
				.setParameter(RADIUS_PARAM,
						Integer.toString(Constants.GEO_QUERY_RADIUS))
				.setParameter(LAT_PARAM,
						Double.toString(location.getLatitude()))
				.setParameter(LONG_PARAM,
						Double.toString(location.getLongitude()))
				.setParameter(INCLUDE_DOCS_PARAM, "true").build();

		return uri;

	}

	private static String buildGeoPath() {
		StringBuilder sb = new StringBuilder();
		sb.append('/')
				.append(Constants.CLOUDANT_DB_NAME)
				.append("/_design/")
				.append(Constants.GEO_VIEW)
				.append("/_geo/")
				.append(Constants.GEO_INDEX);

		return sb.toString();
	}
	
	

}
