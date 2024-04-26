package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.BookCommentsController;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComments;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookCommentsService;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookCommentsController.class)
class BookCommentControllerTest {

    public static final int BOOK_COMMENT_ID_1 = 1;
    public static final int BOOK_COMMENT_ID_2 = 2;
    public static final String ERROR_STRING = "Таких тут нет!";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookCommentsService bookCommentsService;

    @MockBean
    private BookService bookService;

    @DisplayName("Список комментариев книги")
    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        List<BookComments> bookComments = List.of(
                new BookComments(BOOK_COMMENT_ID_1, "Comment_1", new Book()),
                new BookComments(BOOK_COMMENT_ID_2, "Comment_2", new Book()));
        given(bookCommentsService.findAllForBook(BOOK_COMMENT_ID_1)).willReturn(bookComments);

        Book book = new Book(BOOK_COMMENT_ID_1, "Book_1", new Author(), new Genre(), null);
        given(bookService.findById(BOOK_COMMENT_ID_1)).willReturn(Optional.of(book));

        mvc.perform(get("/api/book_comments/").param("id", String.valueOf(BOOK_COMMENT_ID_1)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookComments", hasSize(BOOK_COMMENT_ID_2)))
                .andExpect(model().attribute("bookComments", equalTo(bookComments)))
                .andExpect(content().string(containsString(bookComments.get(0).getComment())));
    }

    @DisplayName("Получение комментария по id")
    @Test
    void shouldReturnCorrectBookById() throws Exception {
        BookComments bookComment = new BookComments(BOOK_COMMENT_ID_1, "Comment_1", new Book());
        given(bookCommentsService.findById(BOOK_COMMENT_ID_1)).willReturn(Optional.of(bookComment));

        mvc.perform(get("/api/book_comments/editComment").param("id", String.valueOf(BOOK_COMMENT_ID_1)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookComment", equalTo(bookComment)))
                .andExpect(content().string(containsString(bookComment.getComment())));
    }

    @DisplayName("Ошибка при запросе несуществующего id комментария")
    @Test
    void shouldReturnExpectedErrorWhenBookNotFound() throws Exception {
        given(bookCommentsService.findById(BOOK_COMMENT_ID_1)).willReturn(Optional.empty());

        mvc.perform(get("/api/book_comments/editComment").param("id", String.valueOf(BOOK_COMMENT_ID_1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERROR_STRING));
    }

    @DisplayName("Обновление комментария")
    @Test
    void updateBookById() throws Exception {
        BookComments bookComment = new BookComments(BOOK_COMMENT_ID_1, "Comment_1", new Book());
        given(bookCommentsService.save(any(), eq(BOOK_COMMENT_ID_1))).willReturn(bookComment);

        mvc.perform(post("/api/book_comments/editComment")
                        .param("id", String.valueOf(BOOK_COMMENT_ID_1))
                        .flashAttr("bookComment", bookComment))
                .andExpect(redirectedUrl("/api/book_comments/?id=0"));
    }

    @DisplayName("Новый комментарий")
    @Test
    void newBookById() throws Exception {
        BookComments bookComment = new BookComments(BOOK_COMMENT_ID_1, "Comment_1", new Book());
        given(bookCommentsService.save(any(), eq(BOOK_COMMENT_ID_1))).willReturn(bookComment);

        Book book = new Book(BOOK_COMMENT_ID_1, "Book_1", new Author(), new Genre(), null);
        given(bookService.findById(BOOK_COMMENT_ID_1)).willReturn(Optional.of(book));

        mvc.perform(get("/api/book_comments/newComment")
                        .param("id", String.valueOf(BOOK_COMMENT_ID_1))
                        .param("book_id", String.valueOf(BOOK_COMMENT_ID_1))
                        .flashAttr("bookComment", bookComment))
                .andExpect(view().name("editBookComment"));
    }

    @DisplayName("Удаление комментария")
    @Test
    void deleteBookById() throws Exception {
        BookComments bookComment = new BookComments(BOOK_COMMENT_ID_1, "Comment_1",
                new Book(BOOK_COMMENT_ID_1, "Book_1", new Author(), new Genre(), null));

        mvc.perform(post("/api/book_comments/deleteComment")
                        .param("id", String.valueOf(BOOK_COMMENT_ID_1))
                        .flashAttr("bookComment", bookComment))
                .andExpect(redirectedUrl("/api/book_comments/?id=1"));
    }
}