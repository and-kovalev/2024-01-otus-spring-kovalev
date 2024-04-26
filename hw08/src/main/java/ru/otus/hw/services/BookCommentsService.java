package ru.otus.hw.services;

import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.BookComments;

import java.util.List;
import java.util.Optional;

public interface BookCommentsService {
    List<BookComments> findAllForBook(String bookId);

    Optional<BookComments> findById(String id);

    @Transactional
    BookComments insert(String comment, String bookId);

    @Transactional
    BookComments update(String id, String comment);

    @Transactional
    void deleteById(String id);
}
