/*
 * Licensed Materials - Property of IBM © Copyright IBM Corporation 2015. All
 * Rights Reserved. This sample program is provided AS IS and may be used,
 * executed, copied and modified without royalty payment by customer (a) for its
 * own instruction and study, (b) in order to develop applications designed to
 * run with an IBM product, either for customer's own internal use or for
 * redistribution by customer, as part of such an application, in customer's own
 * products.
 */

package com.ibm.mil.utils;

public class Constants {
	
	public final static String CLOUDANT_ACCOUNT = "505da9f7-eb2c-4dfe-a682-ee3855cbfc4c-bluemix.cloudant.com";
	public final static String CLOUDANT_USERNAME = "505da9f7-eb2c-4dfe-a682-ee3855cbfc4c-bluemix";
	public final static String CLOUDANT_PASSWORD = "ce834e11cb30072d2525ecc268dcb68b639740250dea8752a1cc7a4cf67bbb5f";
	public final static String CLOUDANT_DB_NAME = "telco";
	public final static String MAIN_DESIGN_DOC = "library";

	public final static String DEFAULT_LOCALE = "DEFAULT_LOCALE";
	

	
	/******* Constants for Cloudant Geospatial ******/
	public final static String GEO_VIEW = "geodd";
	public final static String GEO_INDEX = "geoidx";
	/* GEO_DEFAULT_RADIUS is in meters */
	public final static int GEO_QUERY_RADIUS = 100000;
	public final static int GEO_RETURN_RADIUS = 10000;
	public final static String GEO_DEFAULT_RELATION = "within";
	
}
