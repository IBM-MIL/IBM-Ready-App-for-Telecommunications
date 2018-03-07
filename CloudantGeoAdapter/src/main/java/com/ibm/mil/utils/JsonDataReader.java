package com.ibm.mil.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("rawtypes")
public final class JsonDataReader {
	// Gson instance
	private static Gson gson = new Gson();

	// Logger
	private final static Logger LOGGER = Logger.getLogger(JsonDataReader.class.getName());

	private JsonDataReader() {
		throw new AssertionError("Utilities is non-instantiable");
	}

	/**
	 * Returns all those JSON data records that can be processed (i.e. inserted
	 * into the db) without the need to know the _rev value of a parent record.
	 *
	 * @return
	 */
	public static List<List<Map>> getAllJsonData(String... jsonFiles) {
		List<List<Map>> jsonData = new ArrayList<List<Map>>();
		for (String jsonFile : jsonFiles) {
			List<Map> records = getMapCollection(jsonFile);
			jsonData.add(records);
		}
		return jsonData;
	}

	/**
	 *
	 * @param jsonFile
	 * @return
	 */
	public static List<Map> getMapCollection(String jsonFile) {
		TypeToken<List<Map>> typeToken = new TypeToken<List<Map>>() {
		};
		return getCollection(typeToken, jsonFile);
	}

	/**
	 * Generic method for parsing a JSON data file and returning a collection of
	 * data objects. The type of object in the collection is determined by the
	 * typeToken parameter. The file to be read is specifie in the jsonFile
	 * parameter. If an error occurs while processing the JSON file, an empty
	 * collection is returned.
	 *
	 * @param typeToken
	 * @param jsonFile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends List<U>, U> T getCollection(TypeToken<T> typeToken, String jsonFile) {
		T collection;
		try {
			URL url = JsonDataReader.class.getClassLoader().getResource(jsonFile);
			BufferedReader br = new BufferedReader(new FileReader(url.getFile()));
			collection = gson.fromJson(br, typeToken.getType());
		} catch (IOException ioe) {
			collection = (T) new ArrayList<U>();
			LOGGER.severe("Could not load JSON data file: " + jsonFile);
			LOGGER.severe(ioe.getMessage());
		}
		return collection;
	}
}
