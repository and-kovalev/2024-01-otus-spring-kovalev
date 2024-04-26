package ru.otus.hw.services;

import ru.otus.hw.models.BookComments;

import java.util.List;
import java.util.Optional;

public interface BookCommentsService {
    List<BookComments> findAllForBook(long bookId);

    Optional<BookComments> findById(long id);

    BookComments insert(String comment, long bookId);

    BookComments update(long id, String comment, long bookId);

    void deleteById(long id);

    BookComments save(BookComments book, long bookId);
}
