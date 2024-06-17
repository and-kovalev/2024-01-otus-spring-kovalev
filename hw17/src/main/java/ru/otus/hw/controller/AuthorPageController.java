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

@Controller
@RequiredArgsConstructor
public class AuthorPageController {

    @GetMapping("/authors/")
    public String listAuthorsPage() {
        return "listAuthors";
    }

    @GetMapping("/authors/{id}")
    public String editAuthorPage(@PathVariable(value = "id", required = false) String id, Model model) {
        model.addAttribute("author_id", id);
        return "editAuthor";
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.badRequest().body("Таких тут нет!");
    }
}
