package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.BookComments;
import ru.otus.hw.services.BookCommentsService;
import ru.otus.hw.services.BookService;

@Controller
@RequiredArgsConstructor
public class BookCommentsController {

    private final BookService bookService;

    private final BookCommentsService bookCommentsService;

    @GetMapping("/api/book_comments/")
    public String listBookCommentsPage(@RequestParam("id") long bookId, Model model) {
        var bookComments = bookCommentsService.findAllForBook(bookId);
        model.addAttribute("bookComments", bookComments);
        model.addAttribute("book", bookService.findById(bookId).orElseThrow(NotFoundException::new));
        return "listBookComments";
    }

    @GetMapping("/api/book_comments/editComment")
    public String editBookCommentPage(@RequestParam("id") long id, Model model) {
        BookComments bookComments = bookCommentsService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("bookComment", bookComments);
        model.addAttribute("book", bookComments.getBook());

        return "editBookComment";
    }

    @PostMapping("/api/book_comments/editComment")
    public String saveBook(@ModelAttribute("bookComment") BookComments bookComments,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "editBookComment";
        }

        bookCommentsService.save(bookComments, bookComments.getBook().getId());
        return "redirect:/api/book_comments/?id=" + bookComments.getBook().getId();
    }


    @GetMapping("/api/book_comments/newComment")
    public String newComment(@RequestParam("book_id") long bookId, Model model) {
        BookComments bookComment = new BookComments();
        bookComment.setComment("Comment_");
        var book = bookService.findById(bookId).orElseThrow(NotFoundException::new);
        bookComment.setBook(book);
        model.addAttribute("bookComment", bookComment);
        model.addAttribute("book", book);

        return "editBookComment";
    }

    @GetMapping("/api/book_comments/deleteComment")
    public String deleteBookPage(@RequestParam("id") long id, Model model) {
        BookComments bookComments = bookCommentsService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("bookComment", bookComments);
        var book = bookService.findById(bookComments.getBook().getId()).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);
        return "delBookComment";
    }

    @PostMapping("/api/book_comments/deleteComment")
    public String deleteBook(@Valid @ModelAttribute("bookComment") BookComments bookComments,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "delBookComment";
        }
        var bookId = bookComments.getBook().getId();

        bookCommentsService.deleteById(bookComments.getId());
        return "redirect:/api/book_comments/?id=" + bookId;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.badRequest().body("Таких тут нет!");
    }
}
