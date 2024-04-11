package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public Optional<BookComments> findById(long id) {
        return bookCommentsRepository.findById(id);
    }

    @Transactional
    @Override
    public BookComments insert(String comment, long bookId) {
        return save(0, comment, bookId);
    }

    @Transactional
    @Override
    public BookComments update(long id, String comment, long bookId) {
        return save(id, comment, bookId);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookCommentsRepository.deleteById(id);
    }

    private BookComments save(long id, String comment, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var bookComment = new BookComments(id, comment, book);
        return bookCommentsRepository.save(bookComment);
    }
}
