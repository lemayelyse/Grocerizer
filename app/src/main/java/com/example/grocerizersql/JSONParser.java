package com.example.grocerizersql;

/*
 * Json getter. Fill out description later
 */

// Using Google's GSON library for JSON
import android.util.Log;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class JSONGetter {
    // TODO: define privacy settings
    // TODO: more exceptions

    // Default: package private
    JsonArray getItems(String url) {
        JsonArray items = getJSON(url);
        System.out.println(items);
        return items;
    }

    private JsonArray getJSON(String url) {
        InputStreamReader isr;
        int respCode;

        // Get input stream
        try {
            URL weeklyAd = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) weeklyAd.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Compatible)");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();
            respCode = conn.getResponseCode();
            System.out.println("Response code: " + respCode);

            // TODO: Clean up failure modes and exceptions using resp code
            try { // resp is 200
                isr = new InputStreamReader(conn.getInputStream());
            } catch(FileNotFoundException exception){ // This means the URL is outdated
                Log.e(exception.getMessage(), exception.toString());
                return null;
            }
        } catch (IOException exc) {
            Log.e("IOException", exc.toString());
            exc.printStackTrace();
            return null;
        }

        // Get JSON "items"
        JsonParser parser = new JsonParser(); //from gson
        JsonElement root = parser.parse(isr);
        JsonObject obj = root.getAsJsonObject();
        return obj.getAsJsonArray("items");
    }
}
