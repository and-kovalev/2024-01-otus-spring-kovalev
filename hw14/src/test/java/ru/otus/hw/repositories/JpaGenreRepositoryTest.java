package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.Application;
import ru.otus.hw.models.jpa.JpaGenre;
import ru.otus.hw.repositories.jpa.JpaGenreRepository;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import({Application.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    private List<JpaGenre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("должен загружать жанр по id")
    @ParameterizedTest
    @MethodSource("getDbGenres")
    void shouldReturnCorrectGenreById(JpaGenre testGenre) {
        var actualGenre = repositoryJpa.findById(testGenre.getId());
        var expectedGenre = em.find(JpaGenre.class, testGenre.getId());
        assertThat(actualGenre).isPresent()
                .get()
                .isEqualTo(expectedGenre);
    }

    @DisplayName("должен загружать все жанры")
    @Test
    void shouldReturnAllGenres() {
        var actualGenre = repositoryJpa.findAll();
        assertThat(actualGenre).isNotEmpty()
                .isEqualTo(getDbGenres());
    }

    private static List<JpaGenre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new JpaGenre(id, "Genre_" + id))
                .toList();
    }
}