package com.ynov.mediacity.model;

// Un ouvrage (livre) de la mediatheque.
public class Book {

    private final String id;
    private final String title;

    public Book(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
