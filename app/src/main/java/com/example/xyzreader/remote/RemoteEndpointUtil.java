package com.example.xyzreader.remote;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;

public class RemoteEndpointUtil {
    private static final String TAG = "RemoteEndpointUtil";

    private RemoteEndpointUtil() {
    }

    /**
     * Fetch JSON string from Config.BASE_URL
     * and parse it into JSON array.
     * */
    public static JSONArray fetchJsonArray() {

        // get JSON string from Config.BASE_URL
        String itemsJson;
        try {
            itemsJson = fetchPlainText(Config.BASE_URL);
        } catch (IOException e) {
            Log.e(TAG, "Error fetching items JSON", e);
            return null;
        }

        // parse JSON into JSONArray
        try {
            JSONTokener tokener = new JSONTokener(itemsJson);
            Object val = tokener.nextValue();
            if (!(val instanceof JSONArray)) {
                throw new JSONException("Expected JSONArray");
            }
            return (JSONArray) val;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing items JSON", e);
        }

        return null;
    }

    // helper methods
    static String fetchPlainText(URL url) throws IOException {

        // this is 3d party library
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
