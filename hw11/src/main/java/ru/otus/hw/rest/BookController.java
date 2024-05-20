package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @GetMapping("/api/books/")
    public Flux<BookDto> listBooks() {
        return bookRepository.findAll().map(BookDto::toDto);
    }

    @GetMapping("/api/books/find")
    public Mono<BookDto> findBook(@RequestParam("id") String id) {
        return bookRepository.findById(id).map(BookDto::toDto);
    }

    @PostMapping("/api/books")
    public Mono<BookDto> saveBook(@RequestBody BookDto book) {
        return bookRepository.findById(book.getId())
                .flatMap(fbook ->
                        authorRepository.findById(book.getAuthor().getId())
                                .flatMap(author -> genreRepository.findById(book.getGenre().getId())
                                        .flatMap(genre -> bookRepository.save(book.toDomainObject().setAuthor(author).setGenre(genre))
                                        )
                                )
                )
                .map(BookDto::toDto);
    }

    @PostMapping("/api/books/insertBook")
    public Mono<BookDto> insertBook(@RequestBody BookDto book) {
        return authorRepository.findById(book.getAuthor().getId())
                                .flatMap(author -> genreRepository.findById(book.getGenre().getId())
                                        .flatMap(genre -> bookRepository.insert(book.toDomainObject().setAuthor(author).setGenre(genre)))
                                )
                .map(BookDto::toDto);
    }

    @PostMapping("/api/books/deleteBook")
    public Mono<Void> deleteBook(@RequestBody BookDto book) {
        return bookRepository.deleteById(book.getId());
    }
}
