package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.mongo.MongoBookComments;

import java.util.List;

public interface MongoBookCommentsRepository extends MongoRepository<MongoBookComments, String> {
    List<MongoBookComments> findAllByBookId(String bookId);

    void deleteByBookId(String id);
}
