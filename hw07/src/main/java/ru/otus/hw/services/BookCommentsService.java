package ru.otus.hw.services;

import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.BookComments;

import java.util.List;
import java.util.Optional;

public interface BookCommentsService {
    List<BookComments> findAllForBook(long bookId);

    Optional<BookComments> findById(long id);

    @Transactional
    BookComments insert(String comment, long bookId);

    @Transactional
    BookComments update(long id, String comment, long bookId);

    @Transactional
    void deleteById(long id);
}
