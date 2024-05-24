package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.BookComments;
import ru.otus.hw.services.BookCommentsService;
import ru.otus.hw.services.BookService;

@Controller
@RequiredArgsConstructor
public class BookCommentsPageController {

    private final BookService bookService;

    private final BookCommentsService bookCommentsService;

    @GetMapping("/book_comments/")
    public String listBookCommentsPage(@RequestParam("id") long bookId, Model model) {
        model.addAttribute("book_id", bookId);
        return "listBookComments";
    }

    @PostAuthorize("hasRole('USER')")
    @GetMapping("/book_comments/{id}")
    public String editBookCommentPage(@PathVariable(value = "id", required = false) long id, Model model) {
        BookComments bookComments = bookCommentsService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("bookComment", bookComments);
        model.addAttribute("book", bookComments.getBook());

        return "editBookComment";
    }

    @PostAuthorize("hasRole('USER')")
    @GetMapping("/book_comments/newComment")
    public String newComment(@RequestParam("book_id") long bookId, Model model) {
        BookComments bookComment = new BookComments();
        bookComment.setComment("Comment_");
        var book = bookService.findById(bookId).orElseThrow(NotFoundException::new);
        bookComment.setBook(book);
        model.addAttribute("bookComment", bookComment);
        model.addAttribute("book", book);

        return "editBookComment";
    }

    @PostAuthorize("hasRole('ADMIN')")
    @GetMapping("/book_comments/deleteComment")
    public String deleteBookPage(@RequestParam("id") long id, Model model) {
        BookComments bookComments = bookCommentsService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("bookComment", bookComments);
        var book = bookService.findById(bookComments.getBook().getId()).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);
        return "delBookComment";
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.badRequest().body("Таких тут нет!");
    }
}
