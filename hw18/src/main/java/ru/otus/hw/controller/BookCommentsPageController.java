package ru.otus.hw.controller;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
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
    @RateLimiter(name = "listBookCommentsPage")
    public String listBookCommentsPage(@RequestParam("id") long bookId, Model model) {
        model.addAttribute("book_id", bookId);
        return "listBookComments";
    }

    @GetMapping("/book_comments/editComment")
    @RateLimiter(name = "editBookCommentPage")
    public String editBookCommentPage(@RequestParam("id") long id, Model model) {
        BookComments bookComments = bookCommentsService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("bookComment", bookComments);
        model.addAttribute("book", bookComments.getBook());

        return "editBookComment";
    }

    @GetMapping("/book_comments/newComment")
    @RateLimiter(name = "newComment")
    public String newComment(@RequestParam("book_id") long bookId, Model model) {
        BookComments bookComment = new BookComments();
        bookComment.setComment("Comment_");
        var book = bookService.findById(bookId).orElseThrow(NotFoundException::new);
        bookComment.setBook(book);
        model.addAttribute("bookComment", bookComment);
        model.addAttribute("book", book);

        return "editBookComment";
    }

    @GetMapping("/book_comments/deleteComment")
    @RateLimiter(name = "deleteBookPage")
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

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<String> handleTooManyRequest(RequestNotPermitted ex) {
        return ResponseEntity.badRequest().body("Too Many Requests - Retry After 1 Minute");
    }
}
