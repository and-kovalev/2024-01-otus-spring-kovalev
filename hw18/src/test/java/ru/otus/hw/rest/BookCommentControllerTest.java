package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookCommentsDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComments;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookCommentsService;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookCommentsController.class)
class BookCommentControllerTest {

    public static final int BOOK_COMMENT_ID_1 = 1;
    public static final int BOOK_COMMENT_ID_2 = 2;
    public static final String ERROR_STRING = "Таких тут нет!";
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookCommentsService bookCommentsService;

    @MockBean
    private BookService bookService;

    @DisplayName("Список комментариев книги")
    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        List<BookComments> bookComments = List.of(
                new BookComments(BOOK_COMMENT_ID_1, "Comment_1",
                        new Book(BOOK_COMMENT_ID_1, "Book_1", new Author(), new Genre(), null)),
                new BookComments(BOOK_COMMENT_ID_2, "Comment_2",
                        new Book(BOOK_COMMENT_ID_2, "Book_2", new Author(), new Genre(), null)));
        given(bookCommentsService.findAllForBook(BOOK_COMMENT_ID_1)).willReturn(bookComments);

        Book book = new Book(BOOK_COMMENT_ID_1, "Book_1", new Author(), new Genre(), null);
        given(bookService.findById(BOOK_COMMENT_ID_1)).willReturn(Optional.of(book));

        mvc.perform(get("/api/book_comments/list").param("id", String.valueOf(BOOK_COMMENT_ID_1)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookComments.stream().map(BookCommentsDto::toDto).toList())));
    }

    @DisplayName("Обновление комментария")
    @Test
    void updateBookById() throws Exception {
        BookComments bookComment = new BookComments(BOOK_COMMENT_ID_1, "Comment_1",
                new Book(BOOK_COMMENT_ID_1, "Book_1", new Author(), new Genre(), null));
        given(bookCommentsService.save(any())).willReturn(bookComment);

        mvc.perform(post("/api/book_comments/editComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(BookCommentsDto.toDto(bookComment))))
                .andExpect(content().json(mapper.writeValueAsString(BookCommentsDto.toDto(bookComment))));
    }

    @DisplayName("Удаление комментария")
    @Test
    void deleteBookById() throws Exception {
        BookComments bookComment = new BookComments(BOOK_COMMENT_ID_1, "Comment_1",
                new Book(BOOK_COMMENT_ID_1, "Book_1", new Author(), new Genre(), null));

        mvc.perform(post("/api/book_comments/deleteComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(BookCommentsDto.toDto(bookComment))))
                .andExpect(status().isOk());
    }
}