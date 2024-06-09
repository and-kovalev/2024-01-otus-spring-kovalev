package ru.otus.hw.services;

import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.mongo.MongoBookComments;

import java.util.List;
import java.util.Optional;

public interface BookCommentsService {
    List<MongoBookComments> findAllForBook(String bookId);

    Optional<MongoBookComments> findById(String id);

    @Transactional
    MongoBookComments insert(String comment, String bookId);

    @Transactional
    MongoBookComments update(String id, String comment);

    @Transactional
    void deleteById(String id);
}
