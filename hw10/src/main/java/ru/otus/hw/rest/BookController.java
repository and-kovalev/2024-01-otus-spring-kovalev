package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
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
    public List<BookDto> listBooks() {
        return bookService.findAll().stream().map(BookDto::toDto).toList();
    }

    @PostMapping("/api/books/editBook")
    public BookDto saveBook(@RequestBody BookDto bookDto) {
        var savedBook = bookService.save(bookDto);
        return BookDto.toDto(savedBook);
    }

    @PostMapping("/api/books/deleteBook")
    public void deleteBook(@RequestBody BookDto bookDto) {
        bookService.deleteById(bookDto.getId());
    }
}
