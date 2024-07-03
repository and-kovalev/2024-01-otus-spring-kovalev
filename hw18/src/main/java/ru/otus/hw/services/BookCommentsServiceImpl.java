package ru.otus.hw.services;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookCommentsDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.BookComments;
import ru.otus.hw.repositories.BookCommentsRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookCommentsServiceImpl implements BookCommentsService {
    private final BookCommentsRepository bookCommentsRepository;

    private final BookRepository bookRepository;

    private final BookService bookService;


    @Transactional(readOnly = true)
    @Override
    @RateLimiter(name = "findAllForBook")
    public List<BookComments> findAllForBook(long bookId) {
        var book = bookService.findById(bookId);
        if (book.isPresent()) {
            if (!book.get().getBookComments().isEmpty()) {
                return book.get().getBookComments();
            }
        }

        return Collections.emptyList();
    }

    @Override
    @RateLimiter(name = "findByIdBook")
    public Optional<BookComments> findById(long id) {
        return bookCommentsRepository.findById(id);
    }

    @Transactional
    @Override
    @RateLimiter(name = "insertBook")
    public BookComments insert(String comment, long bookId) {
        return save(0, comment, bookId);
    }

    @Transactional
    @Override
    @RateLimiter(name = "updateBook")
    public BookComments update(long id, String comment, long bookId) {
        return save(id, comment, bookId);
    }

    @Transactional
    @Override
    @RateLimiter(name = "deleteByIdBookComments")
    public void deleteById(long id) {
        bookCommentsRepository.deleteById(id);
    }

    @Transactional
    @Override
    @RateLimiter(name = "saveBookComments")
    public BookComments save(BookCommentsDto bookCommentsDto) {
        var bookComments = bookCommentsRepository.findById(bookCommentsDto.getId())
                .orElse(new BookComments().setId(bookCommentsDto.getId()));

        bookComments
                .setComment(bookCommentsDto.getComment())
                .setBook(bookService.findById(bookCommentsDto.getBook().getId()).orElse(null));

        return bookCommentsRepository.save(bookComments);
    }

    private BookComments save(long id, String comment, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var bookComment = new BookComments(id, comment, book);
        return bookCommentsRepository.save(bookComment);
    }
}