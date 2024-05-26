package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.hw.models.BookComments;

import java.util.Optional;

public interface BookCommentsRepository extends JpaRepository<BookComments, Long>
        , JpaSpecificationExecutor<BookComments> {
    Optional<BookComments> findById(long id);

    BookComments save(BookComments bookComment);

    void deleteById(long id);
}
