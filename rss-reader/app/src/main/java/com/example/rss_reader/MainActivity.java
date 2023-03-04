package com.example.rss_reader;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private ArticleAdapter articleAdapter;
    private RecyclerView mRecyclerView;

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

    private final FromWebLoader rssLoader = new FromWebLoader(articles -> {
        articles.forEach(article -> {
            Log.d(TAG, article.toString());
        });
            setArticles(articles);
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.articles_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setAdapter(articleAdapter);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerDefaultNetworkCallback(networkCallback);

        rssLoader.execute("https://lenta.ru/rss/articles");
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
}