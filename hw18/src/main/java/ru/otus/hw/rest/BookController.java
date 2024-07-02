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
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/books/list")
    @CircuitBreaker(name = "listBooks")
    public List<BookDto> listBooks() {
        return bookService.findAll().stream().map(BookDto::toDto).toList();
    }

    @PostMapping("/api/books/editBook")
    @CircuitBreaker(name = "saveBook")
    public BookDto saveBook(@RequestBody BookDto bookDto) {
        var savedBook = bookService.save(bookDto);
        return BookDto.toDto(savedBook);
    }

    @PostMapping("/api/books/deleteBook")
    @CircuitBreaker(name = "deleteBook")
    public void deleteBook(@RequestBody BookDto bookDto) {
        bookService.deleteById(bookDto.getId());
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<String> handleCircuitBreakerOpen(CallNotPermittedException ex) {
        return ResponseEntity.badRequest().body("{}");
    }
}
