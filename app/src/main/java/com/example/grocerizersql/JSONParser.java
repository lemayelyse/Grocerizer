package com.example.grocerizersql;

/**
 * Created by lemay on 1/6/2018.
 */

// Using Google's GSON library for JSON
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class JSONParser {
    // TODO: define privacy settings
    // TODO: more exceptions

    // Default: package private
    // Ju Li! Do the thing!
    void doTheThing(String url) {
        JsonArray items = getJSON(url);
        parseItems(items);
    }

    private JsonArray getJSON(String url) {
        InputStreamReader isr = null;

        // Get input stream
        try {
            URL weeklyAd = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) weeklyAd.openConnection();
            conn.connect();
            isr = new InputStreamReader((InputStream)conn.getContent());
        } catch (IOException exc) {
            Log.e("IOException", exc.toString());
            exc.printStackTrace();
        }

        // Get JSON "items"
        JsonParser parser = new JsonParser(); //from gson
        JsonElement root = parser.parse(isr); // Might be null, TODO: deal with exception
        JsonObject obj = root.getAsJsonObject();
        return obj.getAsJsonArray("items");
    }

    private void parseItems(JsonArray items) {
        // For now, just print the whole thing
        // TODO: populate SQL database with relevant items
        System.out.println(items);
    }
}
