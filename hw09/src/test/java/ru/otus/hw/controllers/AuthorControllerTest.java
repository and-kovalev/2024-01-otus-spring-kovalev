package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.AuthorController;
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

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

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

        mvc.perform(get("/api/authors/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("authors", hasSize(AUTHOR_ID_2)))
                .andExpect(model().attribute("authors", equalTo(authors)))
                .andExpect(content().string(containsString(authors.get(0).getFullName())));
    }

    @DisplayName("Получение автора по id")
    @Test
    void shouldReturnCorrectAuthorById() throws Exception {
        Author author = new Author(AUTHOR_ID_1, "Author1");
        given(authorService.findById(AUTHOR_ID_1)).willReturn(Optional.of(author));

        mvc.perform(get("/api/authors/editAuthor").param("id", String.valueOf(AUTHOR_ID_1)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("author", equalTo(author)))
                .andExpect(content().string(containsString(author.getFullName())));
    }

    @DisplayName("Ошибка при запросе несуществующего id автора")
    @Test
    void shouldReturnExpectedErrorWhenAuthorNotFound() throws Exception {
        given(authorService.findById(AUTHOR_ID_1)).willReturn(Optional.empty());

        mvc.perform(get("/api/authors/editAuthor").param("id", String.valueOf(AUTHOR_ID_1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERROR_STRING));
    }

    @DisplayName("Обновление автора")
    @Test
    void updateAuthorById() throws Exception {
        Author author = new Author(AUTHOR_ID_1, "Author12");
        given(authorService.save(any())).willReturn(author);

        mvc.perform(post("/api/authors/editAuthor")
                        .param("id", String.valueOf(AUTHOR_ID_1))
                        .flashAttr("author", author))
                .andExpect(redirectedUrl("/api/authors/"));
    }
}