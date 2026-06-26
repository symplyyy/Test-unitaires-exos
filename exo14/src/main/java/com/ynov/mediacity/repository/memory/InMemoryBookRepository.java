package com.ynov.mediacity.repository.memory;

import com.ynov.mediacity.model.Book;
import com.ynov.mediacity.repository.BookRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryBookRepository implements BookRepository {

    private final Map<String, Book> books = new HashMap<>();

    @Override
    public Book save(Book book) {
        books.put(book.getId(), book);
        return book;
    }

    @Override
    public Optional<Book> findById(String id) {
        return Optional.ofNullable(books.get(id));
    }
}
