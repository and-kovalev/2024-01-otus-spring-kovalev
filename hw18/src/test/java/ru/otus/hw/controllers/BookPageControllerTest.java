package ru.otus.hw.controllers;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.function.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.BookPageController;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookPageController.class)
class BookPageControllerTest {

    public static final int BOOK_ID_1 = 1;
    public static final int BOOK_ID_2 = 2;
    public static final String ERROR_STRING = "Таких тут нет!";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @DisplayName("Список книг")
    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        List<Book> books = List.of(
                new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null),
                new Book(BOOK_ID_2, "Book_2", new Author(), new Genre(), null));
        given(bookService.findAll()).willReturn(books);

        mvc.perform(get("/books/"))
                .andExpect(status().isOk());
    }

    @DisplayName("Получение книги по id")
    @Test
    void shouldReturnCorrectBookById() throws Exception {
        Book book = new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null);
        given(bookService.findById(BOOK_ID_1)).willReturn(Optional.of(book));

        mvc.perform(get("/books/editBook").param("id", String.valueOf(BOOK_ID_1)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", equalTo(book)))
                .andExpect(content().string(containsString(book.getTitle())));
    }

    @DisplayName("Ошибка при запросе несуществующего id книги")
    @Test
    void shouldReturnExpectedErrorWhenBookNotFound() throws Exception {
        given(bookService.findById(BOOK_ID_1)).willReturn(Optional.empty());

        mvc.perform(get("/books/editBook").param("id", String.valueOf(BOOK_ID_1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERROR_STRING));
    }

    @DisplayName("Новая книга")
    @Test
    void newBookById() throws Exception {
        Book book = new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null);
        given(bookService.save(any())).willReturn(book);

        mvc.perform(get("/books/newBook")
                        .param("id", String.valueOf(BOOK_ID_1))
                        .flashAttr("book", book))
                .andExpect(view().name("editBook"));
    }

    @DisplayName("Ошибка при установленном RateLimiter")
    @Test
    public void testRateLimiter(){
        RateLimiterConfig config = RateLimiterConfig.custom()
                .timeoutDuration(Duration.ofMillis(100))
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(1)
                .build();

        RateLimiter rateLimiter = RateLimiter.of("listBooksPage", config);

        Callable<List<Book>> restrictedSupplier = RateLimiter
                .decorateCallable(rateLimiter, () -> bookService.findAll());

        RequestNotPermitted exception = assertThrows(RequestNotPermitted.class,
                () -> IntStream.rangeClosed(1,2)
                        .forEach(i -> {
                            Try<List<Book>> aTry = Try.call(restrictedSupplier);
                            try {
                                System.out.println(aTry.get());
                            } catch (Exception e) {
                                throw RequestNotPermitted.createRequestNotPermitted(rateLimiter);
                            }
                        }));

        assertEquals("RateLimiter 'listBooksPage' does not permit further calls", exception.getMessage());
    }

}