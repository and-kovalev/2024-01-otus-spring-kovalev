package ru.otus.hw.controller;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

@Controller
@RequiredArgsConstructor
public class AuthorPageController {

    private final AuthorService authorService;

    @GetMapping("/authors/")
    public String listAuthorsPage() {
        return "listAuthors";
    }

    @GetMapping("/authors/editAuthor")
    public String editAuthorPage(@RequestParam("id") long id, Model model) {
        Author author = authorService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("author", author);
        return "editAuthor";
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
