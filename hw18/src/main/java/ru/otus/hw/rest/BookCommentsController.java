package ru.otus.hw.rest;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookCommentsDto;
import ru.otus.hw.services.BookCommentsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookCommentsController {

    private final BookCommentsService bookCommentsService;

    @GetMapping("/api/book_comments/list")
    @CircuitBreaker(name = "listBookComments")
    public List<BookCommentsDto> listBookComments(@RequestParam("id") long bookId) {
        return bookCommentsService.findAllForBook(bookId).stream().map(BookCommentsDto::toDto).toList();
    }

    @PostMapping("/api/book_comments/editComment")
    @CircuitBreaker(name = "saveComment")
    public BookCommentsDto saveComment(@RequestBody BookCommentsDto bookCommentsDto) {
        var savedBook = bookCommentsService.save(bookCommentsDto);
        return BookCommentsDto.toDto(savedBook);
    }

    @PostMapping("/api/book_comments/deleteComment")
    @CircuitBreaker(name = "deleteComment")
    public void deleteComment(@RequestBody BookCommentsDto bookCommentsDto) {
        bookCommentsService.deleteById(bookCommentsDto.getId());
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<String> handleCircuitBreakerOpen(CallNotPermittedException ex) {
        return ResponseEntity.badRequest().body("{}");
    }
}
