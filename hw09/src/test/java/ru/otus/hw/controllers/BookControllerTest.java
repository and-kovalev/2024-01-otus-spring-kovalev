package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.BookController;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

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

        mvc.perform(get("/api/books/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", hasSize(BOOK_ID_2)))
                .andExpect(model().attribute("books", equalTo(books)))
                .andExpect(content().string(containsString(books.get(0).getTitle())));
    }

    @DisplayName("Получение книги по id")
    @Test
    void shouldReturnCorrectBookById() throws Exception {
        Book book = new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null);
        given(bookService.findById(BOOK_ID_1)).willReturn(Optional.of(book));

        mvc.perform(get("/api/books/editBook").param("id", String.valueOf(BOOK_ID_1)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", equalTo(book)))
                .andExpect(content().string(containsString(book.getTitle())));
    }

    @DisplayName("Ошибка при запросе несуществующего id книги")
    @Test
    void shouldReturnExpectedErrorWhenBookNotFound() throws Exception {
        given(bookService.findById(BOOK_ID_1)).willReturn(Optional.empty());

        mvc.perform(get("/api/books/editBook").param("id", String.valueOf(BOOK_ID_1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERROR_STRING));
    }

    @DisplayName("Обновление книги")
    @Test
    void updateBookById() throws Exception {
        Book book = new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null);
        given(bookService.save(any())).willReturn(book);

        mvc.perform(post("/api/books/editBook")
                        .param("id", String.valueOf(BOOK_ID_1))
                        .flashAttr("book", book))
                .andExpect(redirectedUrl("/api/books/"));
    }

    @DisplayName("Новая книга")
    @Test
    void newBookById() throws Exception {
        Book book = new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null);
        given(bookService.save(any())).willReturn(book);

        mvc.perform(get("/api/books/newBook")
                        .param("id", String.valueOf(BOOK_ID_1))
                        .flashAttr("book", book))
                .andExpect(view().name("editBook"));
    }

    @DisplayName("Удаление книги")
    @Test
    void deleteBookById() throws Exception {
        mvc.perform(post("/api/books/deleteBook")
                        .param("id", String.valueOf(BOOK_ID_1)))
                .andExpect(redirectedUrl("/api/books/"));
    }
}