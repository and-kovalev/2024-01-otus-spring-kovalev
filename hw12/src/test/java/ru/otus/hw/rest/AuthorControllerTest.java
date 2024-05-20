package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    public static final int AUTHOR_ID_1 = 1;
    public static final int AUTHOR_ID_2 = 2;
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorService authorService;


    @DisplayName("Список авторов")
    @WithMockUser(username = "USER", authorities = {"ROLE_USER"})
    @Test
    void shouldReturnCorrectAuthorsList() throws Exception {
        List<Author> authors = List.of(new Author(AUTHOR_ID_1, "Author1"), new Author(AUTHOR_ID_2, "Author2"));
        given(authorService.findAll()).willReturn(authors);

        mvc.perform(get("/api/authors/"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authors)));
    }

    @DisplayName("Обновление автора")
    @Test
    @WithMockUser(username = "USER", authorities = {"ROLE_USER"})
    void updateAuthorById() throws Exception {
        Author author = new Author(AUTHOR_ID_1, "Author12");
        given(authorService.save(any())).willReturn(author);

        mvc.perform(post("/api/authors/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(AuthorDto.toDto(author))))
                .andExpect(content().json(mapper.writeValueAsString(author)));
    }

    @DisplayName("Ошибка авторизации на списке авторов")
    @Test
    void AuthorsListWith401() throws Exception {
        List<Author> authors = List.of(new Author(AUTHOR_ID_1, "Author1"), new Author(AUTHOR_ID_2, "Author2"));
        given(authorService.findAll()).willReturn(authors);

        mvc.perform(get("/api/authors/"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Ошибка авторизации на обновлении автора")
    @Test
    void AuthorsSaveWith401() throws Exception {
        Author author = new Author(AUTHOR_ID_1, "Author12");
        given(authorService.save(any())).willReturn(author);

        mvc.perform(post("/api/authors/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(AuthorDto.toDto(author))))
                .andExpect(status().isUnauthorized());
    }
}