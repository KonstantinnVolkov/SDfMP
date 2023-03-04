package com.example.rss_reader.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XmlParser implements Runnable{

    private final XmlPullParserFactory xmlFactoryObject;
    private final XmlPullParser parser;

    {
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            parser = xmlFactoryObject.newPullParser();
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {

    }
}
