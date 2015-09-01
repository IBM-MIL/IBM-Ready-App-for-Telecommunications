/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
*/

package com.ibm.mil.readyapps.telco.adapters;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.json.java.JSONObject;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.worklight.core.auth.OAuthSecurity;
import com.worklight.core.oauth.api.OAuthSecurityContext;
import com.worklight.core.oauth.api.OAuthUserIdentity;

/**
 * TelcoUserAdapterResource this class is for demo purposes only and
 * demonstrates the typical functions that a client would need to edit and 
 * display information about a user. 
 * 
 * @tannerpreiss
 *
 */
@Path("/users")
public class TelcoUserAdapterResource {
		
	//Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(TelcoUserAdapterResource.class.getName());

    //Define the server api to be able to perform server operations
    WLServerAPI api = WLServerAPIProvider.getWLServerAPI();

	/* Path for method: "<server address>/TelcoReadyAppMFP/adapters/TelcoUserAdapter/users/secret" */
    @GET
    @Path("/{user_id}/secret")
    @Produces(MediaType.APPLICATION_JSON)
    @OAuthSecurity(scope="AdapterAuthenticationRealm")
    public Response getSecretData(@PathParam("user_id") String userId) {
    	OAuthSecurityContext securityContext = api.getSecurityAPI().getSecurityContext();
    	OAuthUserIdentity identity = securityContext.getUserIdentity();
    	
    	JSONObject response = new JSONObject();
    	response.put("secretData", "This is the secret Data...");
    	if (identity != null) { 
    		response.put("userIdentity", identity.toString());
    	}
    	
    	return Response.ok(response).build();
    }
    
    
	/* Path for method: "<server address>/TelcoReadyAppMFP/adapters/TelcoUserAdapter/users/{user_id}" */
    @GET
    @Path("/{user_id}")
    @OAuthSecurity(enabled=false)
    public Response getUser(@PathParam("user_id") String userId) {
		return Response.ok("getUser() for user " + userId + " was successful.").build();
    }
    
	/* Path for method: "<server address>/TelcoReadyAppMFP/adapters/TelcoUserAdapter/users/{user_id}" */
    @PUT
    @Path("/{user_id}")
    public Response updateUser(@PathParam("user_id") String userId) {
		return Response.ok("updateUser() for user " + userId + " was successful.").build();
    }
    
    /** --------------------ADDONS ----------------------**/
	/* Path for method: "<server address>/TelcoReadyAppMFP/adapters/TelcoUserAdapter/users/{user_id}/addons" */
    @GET
    @Path("/{user_id}/addons")
    public Response getUserAddons(@PathParam("user_id") String userId) {
		return Response.ok("getUserAddons() for user " + userId + " was successful.").build();
    }
    
	/* Path for method: "<server address>/TelcoReadyAppMFP/adapters/TelcoUserAdapter/users/{user_id}/addons" */
    @POST
    @Path("/{user_id}/addons")
    public Response updateUserAddons(@PathParam("user_id") String userId) {
		return Response.ok("updateUserAddons() for user " + userId + " was successful.").build();
    }
    
	/** --------------------OFFFERS ---------------------**/
	/* Path for method: "<server address>/TelcoReadyAppMFP/adapters/TelcoUserAdapter/users/{user_id}/offers" */
	@GET
	@Path("/{user_id}/offers")
	public Response getUserOffers(@PathParam("user_id") String userId) {
		return Response.ok("updateUserOffers() for user " + userId + " was successful.").build();
	}
	
	/* Path for method: "<server address>/TelcoReadyAppMFP/adapters/TelcoUserAdapter/users/{user_id}/offers" */
	@POST
	@Path("/{user_id}/offers")
	public Response updateUserOffers(@PathParam("user_id") String userId) {
		return Response.ok("updateUserOffers() for user " + userId + " was successful.").build();
	}
	
	/* Path for method: "<server address>/TelcoReadyAppMFP/adapters/TelcoUserAdapter/users/{user_id}/offers/recommended" */
	@GET
	@Path("/{user_id}/offers/recommended")
	public Response getRecommendedOffers(@PathParam("user_id") String userId) {
		return Response.ok("getRecommendedOffers() for user " + userId + " was successful.").build();
	}
	
}
