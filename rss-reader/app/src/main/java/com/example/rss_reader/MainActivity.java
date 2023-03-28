package com.example.rss_reader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.example.rss_reader.loaders.FromFileLoader;
import com.example.rss_reader.loaders.FromWebLoader;
import com.example.rss_reader.loaders.RssLoader;
import com.example.rss_reader.models.Article;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RssLoader rssLoader;
    private RecyclerView mRecyclerView;
    private ArticleAdapter articleAdapter;
    private ConnectivityManager connectivityManager;

    //Internet connectivity callback
    private final ConnectivityManager.NetworkCallback networkCallback =
            new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            rssLoader = new FromWebLoader(
                    MainActivity.this::setArticles
            );
            rssLoader.execute("https://lenta.ru/rss/articles");
//            rssLoader.execute("https://news.ru/rss/");
//            rssLoader.execute("https://habr.com/ru/rss/flows/develop/all/?fl=ru");
            Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLost(@NonNull Network network) {
            rssLoader = new FromFileLoader(
                    MainActivity.this::setArticles
            );
            rssLoader.execute("/rss-data.xml");
            Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.articles_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setAdapter(articleAdapter);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (isNetworkAvailable()) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
            rssLoader = new FromWebLoader(
                    this::setArticles
            );
            rssLoader.execute("https://lenta.ru/rss/articles");
        } else {
            rssLoader = new FromFileLoader(
                    MainActivity.this::setArticles
            );
            rssLoader.execute("/rss-data.xml");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    private void setArticles(List<Article> articles) {
        if (articleAdapter == null) {
            articleAdapter = new ArticleAdapter(getApplicationContext(), articles);
            mRecyclerView.setAdapter(articleAdapter);
        } else {
            articleAdapter.setArticles(articles);
            articleAdapter.notifyDataSetChanged();
        }
    }

    private boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo =
                connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}