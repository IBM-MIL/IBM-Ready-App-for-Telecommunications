package com.ibm.mil.readyapps.telco.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility functions for reading a local JSON file from the assets folder and serializing into
 * specified data objects.
 */
public final class JsonUtils {

    private JsonUtils() {
        throw new AssertionError(JsonUtils.class.getName() + " is non-instantiable");
    }

    /** Reads in a JSON file and serializes it with the specified token */
    public static <T> T parseJsonFile(Context ctx, String filename, TypeToken<T> token) {
        return new Gson().fromJson(parseJsonFile(ctx, filename), token.getType());
    }

    /** Reads in a JSON file; returns raw JSON String */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String parseJsonFile(Context ctx, String filename) {
        String json;
        try {
            InputStream stream = ctx.getAssets().open(filename);
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            stream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }

}
