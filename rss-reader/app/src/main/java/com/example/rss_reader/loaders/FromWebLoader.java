package com.example.rss_reader.loaders;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;

import com.example.rss_reader.models.Article;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FromWebLoader extends AsyncTask<String, Void, List<Article>> {

    private final RssFeedCallback callback;
    private final XmlPullParser parser;

    public FromWebLoader(RssFeedCallback callback) {
        this.callback = callback;
        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            parser = xmlFactoryObject.newPullParser();
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<Article> doInBackground(String... urls) {
        final String urlStr = urls[0];
        HttpURLConnection conn = null;
        InputStream is = null;
        List<Article> articles = new ArrayList<>();
        try {
            //Establish connection to the RSS feed URL
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = conn.getInputStream();
                parser.setInput(is, null);
                int eventType = parser.getEventType();
                Article article = null;
                while(eventType != XmlPullParser.END_DOCUMENT) {
                    String name = parser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if (name.equalsIgnoreCase("item")) {
                                article = new Article();
                            } else if (article != null) {
                                if (name.equalsIgnoreCase("title")) {
                                    article.setTitle(parser.nextText());
                                } else if (name.equalsIgnoreCase("description")) {
                                    article.setDescription(parser.nextText());
                                } else if (name.equalsIgnoreCase("link")) {
                                    article.setLink(parser.nextText());
                                } else if (name.equalsIgnoreCase("pubDate")) {
                                    final String dateString = parser.nextText();
                                    article.setPublishedDate(new Date(dateString));
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (name.equalsIgnoreCase("item") && article != null) {
                                articles.add(article);
                            }
                    }
                    eventType = parser.next();
                }

                is.close();
            }

            conn.disconnect();
        } catch (Exception e) {
            Log.e("RssFeedTask", "Error fetching RSS feed", e);
        }
        return articles;
    }

    @Override
    protected void onPostExecute(List<Article> articles) {
        callback.onRssItemsLoaded(articles);
    }

    public interface RssFeedCallback {
        void onRssItemsLoaded(List<Article> rssItems);
    }
}
