package com.example.rss_reader;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rss_reader.models.Article;

import java.text.DateFormat;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private Context context;
    private List<Article> articles;

    public ArticleAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleViewHolder(
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.article_item_view_adapter,
                                parent,
                                false
                        )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        String formattedDate = DateFormat.getDateInstance().format(article.getPublishedDate());

        holder.articleTitleOutput.setText(article.getTitle());
        holder.articleDescriptionOutput.setText(article.getDescription());
        holder.articlePublishedDate.setText(formattedDate);

        holder.itemView.setOnClickListener(v -> {
            //TODO : open article in webView
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView articleTitleOutput;
        TextView articleDescriptionOutput;
        TextView articlePublishedDate;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            this.articleTitleOutput = itemView.findViewById(R.id.article_title);
            this.articleDescriptionOutput = itemView.findViewById(R.id.article_description);
            this.articlePublishedDate = itemView.findViewById(R.id.article_published_date);
        }
    }
}