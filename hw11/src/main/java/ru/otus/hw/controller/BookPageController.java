package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.Book;

@Controller
@RequiredArgsConstructor
public class BookPageController {

    @GetMapping("/books/")
    public String listBooksPage() {
        return "listBooks";
    }

    @GetMapping("/books/{id}")
    public String editBookPage(@PathVariable(value = "id", required = false) String id, Model model) {
        model.addAttribute("book_id", id);

        return "editBook";
    }

    @GetMapping("/books/deleteBook")
    public String deleteBookPage(@RequestParam("id") String id, Model model) {
        model.addAttribute("book_id", id);
        return "delBook";
    }

    @GetMapping("/books/newBook")
    public String newBookPage(Model model) {
        Book book = new Book();
        book.setTitle("Book_");
        model.addAttribute("book", book);

        return "insertBook";
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.badRequest().body("Таких тут нет!");
    }
}
