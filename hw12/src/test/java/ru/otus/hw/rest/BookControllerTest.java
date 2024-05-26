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
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    public static final int BOOK_ID_1 = 1;
    public static final int BOOK_ID_2 = 2;
    public static final String ERROR_STRING = "Таких тут нет!";
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @DisplayName("Список книг")
    @Test
    @WithMockUser(username = "USER", authorities = {"ROLE_USER"})
    void shouldReturnCorrectBooksList() throws Exception {
        List<Book> books = List.of(
                new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null),
                new Book(BOOK_ID_2, "Book_2", new Author(), new Genre(), null));
        given(bookService.findAll()).willReturn(books);

        mvc.perform(get("/api/books/"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(books.stream().map(BookDto::toDto).toList())));
    }

    @DisplayName("Обновление книги")
    @Test
    @WithMockUser(username = "USER", authorities = {"ROLE_USER"})
    void updateBookById() throws Exception {
        Book book = new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null);
        given(bookService.save(any())).willReturn(book);

        mvc.perform(post("/api/books/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(BookDto.toDto(book))))
                .andExpect(content().json(mapper.writeValueAsString(BookDto.toDto(book))));
    }

    @DisplayName("Удаление книги")
    @Test
    @WithMockUser(username = "USER", authorities = {"ROLE_USER"})
    void deleteBookById() throws Exception {
        Book book = new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null);

        mvc.perform(post("/api/books/deleteBook")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(BookDto.toDto(book))))
                .andExpect(status().isOk());
    }

    @DisplayName("Список книг")
    @Test
    void BooksListWith401() throws Exception {
        List<Book> books = List.of(
                new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null),
                new Book(BOOK_ID_2, "Book_2", new Author(), new Genre(), null));
        given(bookService.findAll()).willReturn(books);

        mvc.perform(get("/api/books/"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Обновление книги")
    @Test
    void updateBookByIdWith401() throws Exception {
        Book book = new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null);
        given(bookService.save(any())).willReturn(book);

        mvc.perform(post("/api/books/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(BookDto.toDto(book))))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Удаление книги")
    @Test
    void deleteBookByIdWith401() throws Exception {
        Book book = new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null);

        mvc.perform(post("/api/books/deleteBook")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(BookDto.toDto(book))))
                .andExpect(status().isUnauthorized());
    }
}