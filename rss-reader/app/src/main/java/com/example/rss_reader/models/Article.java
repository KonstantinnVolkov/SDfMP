package com.example.rss_reader.models;

import java.util.Date;

import lombok.ToString;

@ToString
public class Article {

    private String title;
    private String description;
    private String link;
    private Date publishedDate;

    public Article() {
    }

    public Article(String title, String description, String link, Date publishedDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }
}
