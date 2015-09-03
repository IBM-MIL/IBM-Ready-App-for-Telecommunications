/*
 * Licensed Materials - Property of IBM © Copyright IBM Corporation 2015. All
 * Rights Reserved. This sample program is provided AS IS and may be used,
 * executed, copied and modified without royalty payment by customer (a) for its
 * own instruction and study, (b) in order to develop applications designed to
 * run with an IBM product, either for customer's own internal use or for
 * redistribution by customer, as part of such an application, in customer's own
 * products.
 */

package com.ibm.mil.cloudant;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Response;
import com.google.gson.Gson;
import com.ibm.mil.utils.AppPropertiesReader;
import com.ibm.mil.utils.Constants;
import com.ibm.mil.utils.JsonDataReader;

/**
 *
 * This class communicates with Cloudant and returns relevant information for
 * the user who has been properly authenticated. The functions in this class
 * will be invoked via the MobileFirst Platform Adapters.
 */
@SuppressWarnings({ "rawtypes", "unused" })
public final class CloudantService {
	private static CloudantService cloudantConnector;
	private Database db;
	private final Gson gson = new Gson();

	// Logger class for logging output
	private static final Logger LOGGER = Logger.getLogger(CloudantService.class.getSimpleName());

	/**
	 * Private constructor. Instantiates instances of cloudant and the message
	 * services.
	 */
	private CloudantService() {
		super();
		connect();
	}

	/**
	 * Create the connection to the Cloudant database that we'll use for this
	 * service.
	 */
	private void connect() {
		CloudantClient cloudant = new CloudantClient(
				AppPropertiesReader.getStringProperty(Constants.CLOUDANT_ACCOUNT),
				AppPropertiesReader.getStringProperty(Constants.CLOUDANT_USERNAME),
				AppPropertiesReader.getStringProperty(Constants.CLOUDANT_PASSWORD));
		db = cloudant.database(AppPropertiesReader.getStringProperty(Constants.CLOUDANT_DB_NAME), true);
		
	}

	/**
	 * returns one and only one instance of the CloudantConnector class
	 *
	 * @return CloudantConnector instance
	 */
	public static CloudantService getInstance() {
		synchronized (CloudantService.class) {
			if (cloudantConnector == null) {
				cloudantConnector = new CloudantService();
			}
		}
		return cloudantConnector;
	}

	/**
	 * getDatabase() returns the cloudant database so that a user can query for
	 * data.
	 *
	 * @return
	 */
	public Database getDatabase() {
		return db;
	}

	/**
	 * Convenience method for deleting all document records from the database
	 * except design documents.
	 */
	public void deleteRecords() {
		List<Map> results = db.view("_all_docs").includeDocs(true).query(Map.class);
		int recordsDeleted = 0;
		for (Map record : results) {
			String id = (String) record.get("_id");
			Response response = db.remove(record);
			id = response.getId();
			LOGGER.info("DELETE: Record with the following _id was deleted: " + id);
			recordsDeleted++;
		}
		LOGGER.info("Number of records to deleted: " + recordsDeleted);
	}

	public void saveFileToCloudant(String filename) {
		db.bulk(JsonDataReader.getMapCollection(filename));
	}
	
	public static void main(String... args) {
		
		CloudantService.getInstance().saveFileToCloudant(Constants.FILENAME_HOTSPOTS);
	}
	
	
	
	
}