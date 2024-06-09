package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.hw.models.jpa.JpaBook;

import java.util.List;
import java.util.Optional;

public interface JpaBookRepository extends JpaRepository<JpaBook, Long>, JpaSpecificationExecutor<JpaBook> {
    Optional<JpaBook> findById(long id);

    @EntityGraph(attributePaths = {"author", "genre"})
    List<JpaBook> findAll();
}
