package ru.otus.hw.rest;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/api/authors/list")
    public List<AuthorDto> listAuthors() {
        return authorService.findAll().stream().map(AuthorDto::toDto).toList();
    }

    @PostMapping("/api/authors/editAuthor")
    public AuthorDto saveAuthor(@RequestBody AuthorDto authorDto) {
        var savedAuthor = authorService.save(authorDto.toDomainObject());
        return AuthorDto.toDto(savedAuthor);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<String> handleCircuitBreakerOpen(RequestNotPermitted ex) {
        return ResponseEntity.badRequest().body("{}");
    }
}