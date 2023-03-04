package com.example.rss_reader;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.rss_reader.loaders.FromWebLoader;
import com.example.rss_reader.models.Article;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Internet connectivity callback
    private final ConnectivityManager.NetworkCallback networkCallback
            = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLost(@NonNull Network network) {
            Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
        }
    };

    private ConnectivityManager connectivityManager;

    private final FromWebLoader rssLoader = new FromWebLoader(new FromWebLoader.RssFeedCallback() {
        @Override
        public void onRssItemsLoaded(List<Article> articles) {
            articles.forEach(article -> {
                Log.d(TAG, article.toString());
            });
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerDefaultNetworkCallback(networkCallback);

        rssLoader.execute("https://news.ru/rss/");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }
}