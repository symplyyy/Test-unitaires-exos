package com.ynov.mediacity.repository;

import com.ynov.mediacity.model.Book;

import java.util.Optional;

public interface BookRepository {

    Book save(Book book);

    Optional<Book> findById(String id);
}
