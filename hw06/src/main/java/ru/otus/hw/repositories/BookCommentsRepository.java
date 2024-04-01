package ru.otus.hw.repositories;

import ru.otus.hw.models.BookComments;

import java.util.Optional;

public interface BookCommentsRepository {
    Optional<BookComments> findById(long id);

    BookComments save(BookComments bookComment);

    void deleteById(long id);
}
