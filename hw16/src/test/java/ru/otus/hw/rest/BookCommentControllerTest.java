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
import ru.otus.hw.dto.BookCommentsDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComments;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookCommentsRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyString;

@WebFluxTest(controllers = BookCommentsController.class)
@TestPropertySource(properties = {"mongock.enabled=false"})
class BookCommentControllerTest {

    public static final String BOOK_COMMENT_ID_1 = "1";
    public static final String BOOK_COMMENT_ID_2 = "2";
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private BookCommentsRepository bookCommentsRepository;;

    @MockBean
    private BookRepository bookRepository;

    @DisplayName("Список комментариев книги")
    @Test
    void shouldReturnCorrectBooksList() {
        List<BookComments> bookComments = List.of(
                new BookComments(BOOK_COMMENT_ID_1, "Comment_1",
                        new Book(BOOK_COMMENT_ID_1, "Book_1", new Author(), new Genre())),
                new BookComments(BOOK_COMMENT_ID_2, "Comment_2",
                        new Book(BOOK_COMMENT_ID_2, "Book_2", new Author(), new Genre())));

        given(bookCommentsRepository.findAllByBookId(BOOK_COMMENT_ID_1)).willReturn(Flux.just(bookComments.toArray(BookComments[]::new)));

        var result = webTestClient
                .get().uri("/api/book_comments/?id=".concat(BOOK_COMMENT_ID_1))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookCommentsDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookCommentsDto> stepResult = null;
        for (BookComments comment : bookComments) {
            stepResult = step.expectNext(BookCommentsDto.toDto(comment));
        }

        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("Обновление комментария")
    @Test
    void updateBookById() {
        BookComments bookComment = new BookComments(BOOK_COMMENT_ID_1, "Comment_1",
                new Book(BOOK_COMMENT_ID_1, "Book_1", new Author(), new Genre()));
        given(bookCommentsRepository.save(any())).willReturn(Mono.just(bookComment));
        given(bookCommentsRepository.findById(anyString())).willReturn(Mono.just(bookComment));

        BookCommentsDto bookCommentsDto = new BookCommentsDto(bookComment.getId(),
                bookComment.getComment(),
                BookDto.toDto(bookComment.getBook()));

        var result = webTestClient
                .post().uri("/api/book_comments/editComment")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookCommentsDto)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookCommentsDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookCommentsDto> stepResult = step.expectNext(BookCommentsDto.toDto(bookComment));
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("Удаление комментария")
    @Test
    void deleteBookById() {
        BookComments bookComment = new BookComments(BOOK_COMMENT_ID_1, "Comment_1",
                new Book(BOOK_COMMENT_ID_1, "Book_1", new Author(), new Genre()));
        given(bookCommentsRepository.save(any())).willReturn(Mono.just(bookComment));
        given(bookCommentsRepository.findById(anyString())).willReturn(Mono.just(bookComment));

        BookCommentsDto bookCommentsDto = new BookCommentsDto(bookComment.getId(),
                bookComment.getComment(),
                BookDto.toDto(bookComment.getBook()));

        var result = webTestClient
                .post().uri("/api/book_comments/deleteComment")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookCommentsDto)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<Void> stepResult = step.expectNext();
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }
}