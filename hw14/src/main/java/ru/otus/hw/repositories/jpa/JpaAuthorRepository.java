package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.hw.models.jpa.JpaAuthor;

import java.util.List;
import java.util.Optional;

public interface JpaAuthorRepository extends JpaRepository<JpaAuthor, Long>, JpaSpecificationExecutor<JpaAuthor> {
    List<JpaAuthor> findAll();

    Optional<JpaAuthor> findById(long id);
}
