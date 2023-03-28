package com.example.rss_reader.loaders;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FromFileLoader extends RssLoader{

    public FromFileLoader(RssFeedCallback callback) {
        super(callback);
    }

    @Override
    protected InputStream getResourceInputStream(String path) {
        File rssDataFile = new File(path);
        try {
            return new FileInputStream(rssDataFile);
        } catch (Exception e) {
            Log.e("FromFileLoader.getResourceInputStream", "FIS err", e);
        }
        return null;
    }
}
