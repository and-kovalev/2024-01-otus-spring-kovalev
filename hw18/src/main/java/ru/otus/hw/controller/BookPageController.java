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
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookPageController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/books/")
    @RateLimiter(name = "listBooksPage")
    public String listBooksPage() {
        return "listBooks";
    }

    @GetMapping("/books/editBook")
    @RateLimiter(name = "editBookPage")
    public String editBookPage(@RequestParam("id") long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);

        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);

        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "editBook";
    }

    @GetMapping("/books/deleteBook")
    @RateLimiter(name = "deleteBookPage")
    public String deleteBookPage(@RequestParam("id") long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);
        return "delBook";
    }

    @GetMapping("/books/newBook")
    @RateLimiter(name = "newBookPage")
    public String newBookPage(Model model) {
        Book book = new Book();
        book.setTitle("Book_");
        model.addAttribute("book", book);

        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);

        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "editBook";
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
