/*
 *  Licensed Materials - Property of IBM
 *  5725-I43 (C) Copyright IBM Corp. 2011, 2013. All Rights Reserved.
 *  US Government Users Restricted Rights - Use, duplication or
 *  disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
'use strict';

function onAuthRequired(headers, error) {
	return {
		authRequired : true,
		error : error
	};
}

function submitAuthentication(username, password) {
	var demoUser = {
		username : "user1",
		password : "password1"
	};
	WL.Logger.info("SUBMIT AUTH : " + username + " " + password);
	
	if (username === demoUser.username && password === demoUser.password) {
		var userIdentity = {
			userId : username,
			attributes : {
				customerAttr : 1234
			}
		};

		WL.Server.setActiveUser("AdapterAuthenticationRealm", userIdentity);

		return {
			authRequired : false
		};
	} else {
		return onAuthRequired(null, "Invalid credentials");
	}

}

function onLogout() {
	WL.Logger.info("User logged out.");
}