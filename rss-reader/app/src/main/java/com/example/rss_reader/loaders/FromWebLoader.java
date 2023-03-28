package com.example.rss_reader.loaders;

import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FromWebLoader extends RssLoader {

    public FromWebLoader(RssFeedCallback callback) {
        super(callback);
    }

    @Override
    protected InputStream getResourceInputStream(String path) {
        HttpURLConnection conn;
        try {
            //Establish connection to the RSS feed URL
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return conn.getInputStream();
            }
        } catch (Exception e) {
            Log.e("FromWebLoader.getResourceInputStream", "IS err", e);
        }
        return null;
    }
}
