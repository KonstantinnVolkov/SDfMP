package com.example.rss_reader.loaders;

import android.os.AsyncTask;

import com.example.rss_reader.models.Article;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.List;

public class FromFileLoader extends AsyncTask<Void, Void, List<Article>> {

//    private final XmlPullParserFactory xmlFactoryObject;
//    private final XmlPullParser parser;

    @Override
    protected void onPostExecute(List<Article> articles) {
        super.onPostExecute(articles);
    }

    @Override
    protected List<Article> doInBackground(Void... voids) {
        return null;
    }
}
