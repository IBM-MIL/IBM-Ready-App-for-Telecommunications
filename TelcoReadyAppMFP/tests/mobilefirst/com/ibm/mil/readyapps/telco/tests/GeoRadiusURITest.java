package com.ibm.mil.readyapps.telco.tests;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import com.ibm.mil.cloudant.geospatial.GeoJsonPoint;
import com.ibm.mil.cloudant.geospatial.GeoRadiusURI;

/**
 * GeoRadiusURITest contains a regression test for the GeoRadiusURI.build() method.
 * 
 * @author tannerpreiss
 *
 */
public class GeoRadiusURITest {
	
	private static final GeoJsonPoint DEMO_LOCATION = new GeoJsonPoint(10.1, 10.2);

	/**
	 * 	In the database there are ten points at lat 10.1, long 10.2 and so this test ensures that the
	 * 		uri built by GeoRadiusURI.build() functions properly with a demo query location.		
	 */
	@Test
	public void testBuildGeoQueryURI() {
		


		try {
			URI builtDemoURI = GeoRadiusURI.build(DEMO_LOCATION);
			
			String expectedDemoURI = 
					"https://rdyapp:R3%40dyApps@rdyapp.cloudant.com/ra6_quail_db/_design/geodd/_geo/geoidx"
					+ "?radius=100000"
					+ "&lat=10.1"
					+ "&lon=10.2"
					+ "&include_docs=true";
			
			assertEquals(builtDemoURI.toString(), expectedDemoURI);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("testBuildGeoQueryURI() failed building the CloudantGeo URI");
		}
	}

}
