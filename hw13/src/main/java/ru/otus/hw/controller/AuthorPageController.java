package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostAuthorize("hasRole('USER')")
    @GetMapping("/authors/{id}")
    public String editAuthorPage(@PathVariable(value = "id", required = false)  long id, Model model) {
        Author author = authorService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("author", author);
        return "editAuthor";
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.badRequest().body("Таких тут нет!");
    }
}
