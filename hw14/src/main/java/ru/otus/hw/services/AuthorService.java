package ru.otus.hw.services;

import ru.otus.hw.models.mongo.MongoAuthor;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<MongoAuthor> findAll();

    Optional<MongoAuthor> findById(String id);
}
