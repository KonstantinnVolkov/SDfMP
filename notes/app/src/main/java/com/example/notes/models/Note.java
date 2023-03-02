package com.example.notes.models;

import java.util.Comparator;
import java.util.Date;

import lombok.ToString;

@ToString
public class Note implements Comparator<Note> {

    private String id;
    private String title;
    private String description;
    private Date createdAt;

    @Override
    public int compare(Note o1, Note o2) {
        return o1.getCreatedAt().compareTo(o2.getCreatedAt()) * (-1);
    }

    public Note() {
    }

    public Note(String id, String title, String description, Date createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
