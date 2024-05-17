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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@WebFluxTest(controllers = AuthorController.class)
@TestPropertySource(properties = {"mongock.enabled=false"})
class AuthorControllerTest {

    public static final String AUTHOR_ID_1 = "1";
    public static final String AUTHOR_ID_2 = "2";
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private AuthorRepository authorRepository;

    @DisplayName("Список авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        List<Author> authors = List.of(new Author(AUTHOR_ID_1, "Author1"), new Author(AUTHOR_ID_2, "Author2"));
        given(authorRepository.findAll()).willReturn(Flux.just(authors.toArray(Author[]::new)));

        var result = webTestClient.get().uri("/api/authors/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AuthorDto.class)
                .getResponseBody();
        var step = StepVerifier.create(result);
        StepVerifier.Step<AuthorDto> stepResult = null;
        for (Author author : authors) {
            stepResult = step.expectNext(AuthorDto.toDto(author));
        }

        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("Обновление автора")
    @Test
    void updateAuthorById() {
        Author author = new Author(AUTHOR_ID_1, "Author1");
        AuthorDto authorDto = new AuthorDto(author.getId(),
                author.getFullName());

        given(authorRepository.save(any())).willReturn(Mono.just(author));

        var result = webTestClient
                .post().uri("/api/authors/editAuthor")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authorDto)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AuthorDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<AuthorDto> stepResult = step.expectNext(AuthorDto.toDto(author));
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("Поиск автора")
    @Test
    void findAuthorById() {
        Author author = new Author(AUTHOR_ID_1, "Author1");
        AuthorDto authorDto = new AuthorDto(author.getId(),
                author.getFullName());

        given(authorRepository.findById(anyString())).willReturn(Mono.just(author));

        var result = webTestClient
                .get().uri("/api/authors/find?id=".concat(author.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AuthorDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<AuthorDto> stepResult = step.expectNext(AuthorDto.toDto(author));
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }
}