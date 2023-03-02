package com.example.notes.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBConnection {

    private final static String TASKS_NODE = "notes";
    private static final DatabaseReference NOTES_DATABASE_REFERENCE;

    static {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        NOTES_DATABASE_REFERENCE = firebaseDatabase.getReference()
                .child(TASKS_NODE);
    }

    public static DatabaseReference getDbReference() {
        return NOTES_DATABASE_REFERENCE;
    }
}
