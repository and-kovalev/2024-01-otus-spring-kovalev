package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.AuthorPageController;
import ru.otus.hw.dto.AuthorDto;
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

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorService authorService;

    @DisplayName("Список авторов")
    @Test
    void shouldReturnCorrectAuthorsList() throws Exception {
        List<Author> authors = List.of(new Author(AUTHOR_ID_1, "Author1"), new Author(AUTHOR_ID_2, "Author2"));
        given(authorService.findAll()).willReturn(authors);

        mvc.perform(get("/api/authors/list"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authors)));
    }

    @DisplayName("Обновление автора")
    @Test
    void updateAuthorById() throws Exception {
        Author author = new Author(AUTHOR_ID_1, "Author12");
        given(authorService.save(any())).willReturn(author);

        mvc.perform(post("/api/authors/editAuthor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(AuthorDto.toDto(author))))
                .andExpect(content().json(mapper.writeValueAsString(author)));
    }
}