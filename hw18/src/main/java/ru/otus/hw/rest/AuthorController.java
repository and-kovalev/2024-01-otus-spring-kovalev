package ru.otus.hw.rest;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    @CircuitBreaker(name = "listAuthors")
    public List<AuthorDto> listAuthors() {
        return authorService.findAll().stream().map(AuthorDto::toDto).toList();
    }

    @PostMapping("/api/authors/editAuthor")
    @CircuitBreaker(name = "saveAuthor")
    public AuthorDto saveAuthor(@RequestBody AuthorDto authorDto) {
        var savedAuthor = authorService.save(authorDto.toDomainObject());
        return AuthorDto.toDto(savedAuthor);
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<String> handleCircuitBreakerOpen(CallNotPermittedException ex) {
        return ResponseEntity.badRequest().body("{}");
    }
}
