package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookCommentsDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.repositories.BookCommentsRepository;
import ru.otus.hw.repositories.BookRepository;

@RestController
@RequiredArgsConstructor
public class BookCommentsController {

    private final BookCommentsRepository bookCommentsRepository;
    private final BookRepository bookRepository;

    @GetMapping("/api/book_comments/")
    public Flux<BookCommentsDto> listBookComments(@RequestParam("id") String bookId) {
        return bookCommentsRepository.findAllByBookId(bookId).map(BookCommentsDto::toDto);
    }

    @GetMapping("/api/book_comments/find")
    public Mono<BookCommentsDto> findBookComments(@RequestParam("id") String bookId) {
        return bookCommentsRepository.findById(bookId).map(BookCommentsDto::toDto);
    }

    @PostMapping("/api/book_comments/editComment")
    public Mono<BookCommentsDto> saveComment(@RequestBody BookCommentsDto bookComments) {
        return bookCommentsRepository.findById(bookComments.getId())
                .flatMap(comment -> bookCommentsRepository.save(comment.setComment(bookComments.getComment())))
                .map(BookCommentsDto::toDto);
    }

    @PostMapping("/api/book_comments/insertComment")
    public Mono<BookCommentsDto> insertComment(@RequestBody BookCommentsDto bookComments) {
        return bookRepository.findById(bookComments.getBook().getId())
                .flatMap(book -> bookCommentsRepository.insert(bookComments
                        .setBook(BookDto.toDto(book))
                        .toDomainObject().setBook(book)))
                .map(BookCommentsDto::toDto);
    }

    @PostMapping("/api/book_comments/deleteComment")
    public Mono<Void> deleteComment(@RequestBody BookCommentsDto bookComments) {
        return bookCommentsRepository.deleteById(bookComments.getId());
    }
}
