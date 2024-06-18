package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.BookComments;

@Controller
@RequiredArgsConstructor
public class BookCommentsPageController {

    @GetMapping("/book_comments/")
    public String listBookCommentsPage(@RequestParam("id") String bookId, Model model) {
        model.addAttribute("book_id", bookId);
        return "listBookComments";
    }

    @GetMapping("/book_comments/editComment")
    public String editBookCommentPage(@RequestParam("id") String id, @RequestParam("book_id") String bookId,
                                      Model model) {
        model.addAttribute("bookComment_id", id);
        model.addAttribute("book_id", bookId);

        return "editBookComment";
    }

    @GetMapping("/book_comments/newComment")
    public String newComment(@RequestParam("book_id") String bookId, Model model) {
        BookComments bookComment = new BookComments();
        bookComment.setComment("Comment_");
        model.addAttribute("bookComment", bookComment);
        model.addAttribute("book_id", bookId);

        return "insertBookComment";
    }

    @GetMapping("/book_comments/deleteComment")
    public String deleteBookPage(@RequestParam("id") String id, @RequestParam("book_id") String bookId, Model model) {
        model.addAttribute("bookComment_id", id);
        model.addAttribute("book_id", bookId);
        return "delBookComment";
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.badRequest().body("Таких тут нет!");
    }
}
