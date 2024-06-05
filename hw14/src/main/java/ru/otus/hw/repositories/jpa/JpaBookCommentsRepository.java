package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.hw.models.jpa.JpaBookComments;

import java.util.List;
import java.util.Optional;

public interface JpaBookCommentsRepository extends JpaRepository<JpaBookComments, Long>
        , JpaSpecificationExecutor<JpaBookComments> {

    Optional<JpaBookComments> findById(long id);

    List<JpaBookComments> findAll();
}
