package ru.otus.hw.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@WebFluxTest(controllers = GenreController.class)
@TestPropertySource(properties = {"mongock.enabled=false"})
class GenreControllerTest {

    public static final String GENRE_ID_1 = "1";
    public static final String GENRE_ID_2 = "2";
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private GenreRepository genreRepository;

    @DisplayName("Список жанров")
    @Test
    void shouldReturnCorrectAuthorsList() {
        List<Genre> genres = List.of(new Genre(GENRE_ID_1, "Genre1"), new Genre(GENRE_ID_2, "Genre2"));
        given(genreRepository.findAll()).willReturn(Flux.just(genres.toArray(Genre[]::new)));

        var result = webTestClient.get().uri("/api/genres/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(GenreDto.class)
                .getResponseBody();
        var step = StepVerifier.create(result);
        StepVerifier.Step<GenreDto> stepResult = null;
        for (Genre genre : genres) {
            stepResult = step.expectNext(GenreDto.toDto(genre));
        }

        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }
}