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
import ru.otus.hw.controller.AuthorPageController;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.AuthorService;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorPageController.class)
class AuthorPageControllerTest {

    public static final int AUTHOR_ID_1 = 1;
    public static final int AUTHOR_ID_2 = 2;
    public static final String ERROR_STRING = "Таких тут нет!";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @DisplayName("Список авторов")
    @Test
    void shouldReturnCorrectAuthorsList() throws Exception {
        List<Author> authors = List.of(new Author(AUTHOR_ID_1, "Author1"), new Author(AUTHOR_ID_2, "Author2"));
        given(authorService.findAll()).willReturn(authors);

        mvc.perform(get("/authors/"))
                .andExpect(status().isOk());
    }

    @DisplayName("Получение автора по id")
    @Test
    void shouldReturnCorrectAuthorById() throws Exception {
        Author author = new Author(AUTHOR_ID_1, "Author1");
        given(authorService.findById(AUTHOR_ID_1)).willReturn(Optional.of(author));

        mvc.perform(get("/authors/editAuthor").param("id", String.valueOf(AUTHOR_ID_1)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("author", equalTo(author)))
                .andExpect(content().string(containsString(author.getFullName())));
    }

    @DisplayName("Ошибка при запросе несуществующего id автора")
    @Test
    void shouldReturnExpectedErrorWhenAuthorNotFound() throws Exception {
        given(authorService.findById(AUTHOR_ID_1)).willReturn(Optional.empty());

        mvc.perform(get("/authors/editAuthor").param("id", String.valueOf(AUTHOR_ID_1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERROR_STRING));
    }

    @DisplayName("Ошибка при установленном RateLimiter")
    @Test
    public void testRateLimiter(){
        RateLimiterConfig config = RateLimiterConfig.custom()
                .timeoutDuration(Duration.ofMillis(100))
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(1)
                .build();

        RateLimiter rateLimiter = RateLimiter.of("listAuthorsPage", config);

        Callable<List<Author>> restrictedSupplier = RateLimiter
                .decorateCallable(rateLimiter, () -> authorService.findAll());

        RequestNotPermitted exception = assertThrows(RequestNotPermitted.class,
                () -> IntStream.rangeClosed(1,2)
                        .forEach(i -> {
                            Try<List<Author>> aTry = Try.call(restrictedSupplier);
                            try {
                                System.out.println(aTry.get());
                            } catch (Exception e) {
                                throw RequestNotPermitted.createRequestNotPermitted(rateLimiter);
                            }
                        }));

        assertEquals("RateLimiter 'listAuthorsPage' does not permit further calls", exception.getMessage());
    }
}