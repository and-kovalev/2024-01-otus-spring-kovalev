package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.BookComments;

import java.util.List;

public interface BookCommentsRepository extends MongoRepository<BookComments, String> {
    List<BookComments> findAllByBookId(String bookId);

    void deleteByBookId(String id);
}
