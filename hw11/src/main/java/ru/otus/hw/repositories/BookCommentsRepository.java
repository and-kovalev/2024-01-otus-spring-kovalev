package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.BookComments;

public interface BookCommentsRepository extends ReactiveMongoRepository<BookComments, String> {
    Flux<BookComments> findAllByBookId(String bookId);

    void deleteByBookId(String id);
}
