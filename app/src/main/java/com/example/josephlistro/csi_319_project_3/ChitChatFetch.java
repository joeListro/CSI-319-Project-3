package com.example.josephlistro.csi_319_project_3;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe Listro on 12/4/2016.
 */

public class ChitChatFetch {
    private static final String TAG = "ChitChatFetch";

    private static final String API_KEY = "champlainrocks1878\n";
    private static final int SKIP = 0;
    private static final int LIMIT = 20;
    private static final Uri ENDPOINT = Uri
            .parse("https://www.stepoutnyc.com/chitchat")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "url_s")
            .build();

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<ChitChatMessage> fetchRecentChitChats() {
        String url = buildUrl(SKIP, LIMIT);
        return downloadGalleryItems(url);
    }

    private List<ChitChatMessage> downloadGalleryItems(String url) {
        List<ChitChatMessage> items = new ArrayList<>();

        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return items;
    }

    private String buildUrl(int skip, int limit) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon();

        /* Add optional parameters */
        if (skip != 0) {
            uriBuilder.appendQueryParameter("skip", Integer.toString(skip));
        }

        if (limit != 0) {
            uriBuilder.appendQueryParameter("limit", Integer.toString(limit));
        }

        return uriBuilder.build().toString();
    }

    private void parseItems(List<ChitChatMessage> items, JSONObject jsonBody)
            throws IOException, JSONException {

        /* TODO: Parse json feed into List<ChitChatMessage> */

        return;
    }
}
