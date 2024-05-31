package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.GenrePageController;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.hamcrest.Matchers.*;
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
    @WithMockUser(username = "USER", authorities = {"ROLE_USER"})
    void shouldReturnCorrectGenresList() throws Exception {
        List<Genre> genres = List.of(new Genre(GENRE_ID_1, "Genre1"), new Genre(GENRE_ID_2, "Genre2"));
        given(genreService.findAll()).willReturn(genres);

        mvc.perform(get("/genres/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genres", hasSize(GENRE_ID_2)))
                .andExpect(model().attribute("genres", equalTo(genres)))
                .andExpect(content().string(containsString(genres.get(0).getName())));
    }

    @DisplayName("Ошибка авторизации на списоке жанров")
    @Test
    void GenresListWith401() throws Exception {
        List<Genre> genres = List.of(new Genre(GENRE_ID_1, "Genre1"), new Genre(GENRE_ID_2, "Genre2"));
        given(genreService.findAll()).willReturn(genres);

        mvc.perform(get("/genres/"))
                .andExpect(status().isUnauthorized());
    }
}