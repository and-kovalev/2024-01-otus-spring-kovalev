package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.BookPageController;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
    @WithMockUser(username = "USER", authorities = {"ROLE_USER"})
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
    @WithMockUser(username = "USER", authorities = {"ROLE_USER"})
    void shouldReturnCorrectBookById() throws Exception {
        Book book = new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null);
        given(bookService.findById(BOOK_ID_1)).willReturn(Optional.of(book));

        mvc.perform(get("/books/" + BOOK_ID_1))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", equalTo(book)))
                .andExpect(content().string(containsString(book.getTitle())));
    }

    @DisplayName("Ошибка при запросе несуществующего id книги")
    @Test
    @WithMockUser(username = "USER", authorities = {"ROLE_USER"})
    void shouldReturnExpectedErrorWhenBookNotFound() throws Exception {
        given(bookService.findById(BOOK_ID_1)).willReturn(Optional.empty());

        mvc.perform(get("/books/" + BOOK_ID_1))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERROR_STRING));
    }

    @DisplayName("Новая книга")
    @Test
    @WithMockUser(username = "USER", authorities = {"ROLE_USER"})
    void newBookById() throws Exception {
        Book book = new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null);
        given(bookService.save(any())).willReturn(book);

        mvc.perform(get("/books/newBook")
                        .param("id", String.valueOf(BOOK_ID_1))
                        .flashAttr("book", book))
                .andExpect(view().name("editBook"));
    }

    @DisplayName("Список книг")
    @Test
    void BooksListWith401() throws Exception {
        List<Book> books = List.of(
                new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null),
                new Book(BOOK_ID_2, "Book_2", new Author(), new Genre(), null));
        given(bookService.findAll()).willReturn(books);

        mvc.perform(get("/books/"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Получение книги по id")
    @Test
    void BookByIdWith401() throws Exception {
        Book book = new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null);
        given(bookService.findById(BOOK_ID_1)).willReturn(Optional.of(book));

        mvc.perform(get("/books/" + BOOK_ID_1))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Новая книга")
    @Test
    void newBookByIdWith401() throws Exception {
        Book book = new Book(BOOK_ID_1, "Book_1", new Author(), new Genre(), null);
        given(bookService.save(any())).willReturn(book);

        mvc.perform(get("/books/newBook")
                        .param("id", String.valueOf(BOOK_ID_1))
                        .flashAttr("book", book))
                .andExpect(status().isUnauthorized());
    }
}