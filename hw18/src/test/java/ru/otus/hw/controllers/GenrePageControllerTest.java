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
import ru.otus.hw.controller.GenrePageController;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenrePageController.class)
class GenrePageControllerTest {

    public static final int GENRE_ID_1 = 1;
    public static final int GENRE_ID_2 = 2;
    public static final String ERROR_STRING = "Таких тут нет!";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @DisplayName("Список жанров")
    @Test
    void shouldReturnCorrectGenresList() throws Exception {
        List<Genre> genres = List.of(new Genre(GENRE_ID_1, "Genre1"), new Genre(GENRE_ID_2, "Genre2"));
        given(genreService.findAll()).willReturn(genres);

        mvc.perform(get("/genres/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genres", hasSize(GENRE_ID_2)))
                .andExpect(model().attribute("genres", equalTo(genres)))
                .andExpect(content().string(containsString(genres.get(0).getName())));
    }

    @DisplayName("Ошибка при установленном RateLimiter")
    @Test
    public void testRateLimiter(){
        RateLimiterConfig config = RateLimiterConfig.custom()
                .timeoutDuration(Duration.ofMillis(100))
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(1)
                .build();

        RateLimiter rateLimiter = RateLimiter.of("listGenresPage", config);

        Callable<List<Genre>> restrictedSupplier = RateLimiter
                .decorateCallable(rateLimiter, () -> genreService.findAll());

        RequestNotPermitted exception = assertThrows(RequestNotPermitted.class,
                () -> IntStream.rangeClosed(1,2)
                        .forEach(i -> {
                            Try<List<Genre>> aTry = Try.call(restrictedSupplier);
                            try {
                                System.out.println(aTry.get());
                            } catch (Exception e) {
                                throw RequestNotPermitted.createRequestNotPermitted(rateLimiter);
                            }
                        }));

        assertEquals("RateLimiter 'listGenresPage' does not permit further calls", exception.getMessage());
    }
}