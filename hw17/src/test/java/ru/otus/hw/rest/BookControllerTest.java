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
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@WebFluxTest(controllers = BookController.class)
@TestPropertySource(properties = {"mongock.enabled=false"})
class BookControllerTest {

    public static final String BOOK_ID_1 = "1";
    public static final String BOOK_ID_2 = "2";

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;

    @DisplayName("Список книг")
    @Test
    void shouldReturnCorrectBooksList() {
        List<Book> books = List.of(
                new Book(BOOK_ID_1, "Book_1", new Author(), new Genre()),
                new Book(BOOK_ID_2, "Book_2", new Author(), new Genre()));

        given(bookRepository.findAll()).willReturn(Flux.just(books.toArray(Book[]::new)));

        var result = webTestClient.get().uri("/api/books/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = null;
        for (Book book : books) {
            stepResult = step.expectNext(BookDto.toDto(book));
        }

        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("Обновление книги")
    @Test
    void updateBookById() {
        Author author = new Author(BOOK_ID_1, "Author1");
        Genre genre = new Genre(BOOK_ID_1, "Genre1");

        Book book = new Book(BOOK_ID_1, "Book_1", author, genre);
        BookDto bookDto = new BookDto(book.getId(),
                book.getTitle(),
                AuthorDto.toDto(book.getAuthor()),
                GenreDto.toDto(book.getGenre()));

        given(bookRepository.findById(anyString())).willReturn(Mono.just(book));
        given(authorRepository.findById(anyString())).willReturn(Mono.just(author));
        given(genreRepository.findById(anyString())).willReturn(Mono.just(genre));

        given(bookRepository.save(any())).willReturn(Mono.just(book));


        var result = webTestClient
                .post().uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookDto)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = step.expectNext(BookDto.toDto(book));
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("Добавление книги")
    @Test
    void insertBookById() {
        Author author = new Author(BOOK_ID_1, "Author1");
        Genre genre = new Genre(BOOK_ID_1, "Genre1");

        Book book = new Book(BOOK_ID_1, "Book_1", author, genre);
        BookDto bookDto = new BookDto(book.getId(),
                book.getTitle(),
                AuthorDto.toDto(book.getAuthor()),
                GenreDto.toDto(book.getGenre()));

        given(bookRepository.findById(anyString())).willReturn(Mono.just(book));
        given(authorRepository.findById(anyString())).willReturn(Mono.just(author));
        given(genreRepository.findById(anyString())).willReturn(Mono.just(genre));

        given(bookRepository.insert(any(Book.class))).willReturn(Mono.just(book));


        var result = webTestClient
                .post().uri("/api/books/insertBook")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookDto)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = step.expectNext(BookDto.toDto(book));
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("Удаление книги")
    @Test
    void deleteBookById() {
        Author author = new Author(BOOK_ID_1, "Author1");
        Genre genre = new Genre(BOOK_ID_1, "Genre1");

        Book book = new Book(BOOK_ID_1, "Book_1", author, genre);
        BookDto bookDto = new BookDto(book.getId(),
                book.getTitle(),
                AuthorDto.toDto(book.getAuthor()),
                GenreDto.toDto(book.getGenre()));

        var result = webTestClient
                .post().uri("/api/books/deleteBook")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookDto)
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