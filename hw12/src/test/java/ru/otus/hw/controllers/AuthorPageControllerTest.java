package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.AuthorPageController;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @WithMockUser(username = "USER", authorities = {"ROLE_USER"})
    void shouldReturnCorrectAuthorsList() throws Exception {
        List<Author> authors = List.of(new Author(AUTHOR_ID_1, "Author1"), new Author(AUTHOR_ID_2, "Author2"));
        given(authorService.findAll()).willReturn(authors);

        mvc.perform(get("/authors/"))
                .andExpect(status().isOk());
    }

    @DisplayName("Получение автора по id")
    @Test
    @WithMockUser(username = "USER", authorities = {"ROLE_USER"})
    void shouldReturnCorrectAuthorById() throws Exception {
        Author author = new Author(AUTHOR_ID_1, "Author1");
        given(authorService.findById(AUTHOR_ID_1)).willReturn(Optional.of(author));

        mvc.perform(get("/authors/" + AUTHOR_ID_1))
                .andExpect(status().isOk())
                .andExpect(model().attribute("author", equalTo(author)))
                .andExpect(content().string(containsString(author.getFullName())));
    }

    @DisplayName("Ошибка при запросе несуществующего id автора")
    @Test
    @WithMockUser(username = "USER", authorities = {"ROLE_USER"})
    void shouldReturnExpectedErrorWhenAuthorNotFound() throws Exception {
        given(authorService.findById(AUTHOR_ID_1)).willReturn(Optional.empty());

        mvc.perform(get("/authors/" + AUTHOR_ID_1))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERROR_STRING));
    }

    @DisplayName("Ошибка авторизации на списке авторов")
    @Test
    void AuthorsListWith401() throws Exception {
        List<Author> authors = List.of(new Author(AUTHOR_ID_1, "Author1"), new Author(AUTHOR_ID_2, "Author2"));
        given(authorService.findAll()).willReturn(authors);

        mvc.perform(get("/authors/"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Ошибка авторизации на получении автора по id")
    @Test
    void AuthorByIdWith401() throws Exception {
        Author author = new Author(AUTHOR_ID_1, "Author1");
        given(authorService.findById(AUTHOR_ID_1)).willReturn(Optional.of(author));

        mvc.perform(get("/authors/" + AUTHOR_ID_1))
                .andExpect(status().isUnauthorized());
    }
}