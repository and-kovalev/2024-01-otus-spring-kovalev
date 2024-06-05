package ru.otus.hw.services;

import ru.otus.hw.models.mongo.MongoBook;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<MongoBook> findById(String id);

    List<MongoBook> findAll();

    MongoBook insert(String title, String authorId, String genreId);

    MongoBook update(String id, String title, String authorId, String genreId);

    void deleteById(String id);
}
