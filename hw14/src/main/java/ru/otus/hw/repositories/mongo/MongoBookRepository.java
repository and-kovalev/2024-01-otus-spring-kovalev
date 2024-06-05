package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.mongo.MongoBook;

import java.util.List;
import java.util.Optional;

public interface MongoBookRepository extends MongoRepository<MongoBook, String> {
    Optional<MongoBook> findById(String id);

    List<MongoBook> findAll();

    MongoBook save(MongoBook book);

    void deleteById(String id);
}
