package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.hw.models.jpa.JpaGenre;

import java.util.List;
import java.util.Optional;

public interface JpaGenreRepository extends JpaRepository<JpaGenre, Long>, JpaSpecificationExecutor<JpaGenre> {
    List<JpaGenre> findAll();

    Optional<JpaGenre> findById(long id);
}
