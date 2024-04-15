package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.BookComments;
import ru.otus.hw.repositories.BookCommentsRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookCommentsServiceImpl implements BookCommentsService {
    private final BookCommentsRepository bookCommentsRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public List<BookComments> findAllForBook(String bookId) {
        return bookCommentsRepository.findAllByBookId(bookId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookComments> findById(String id) {
        return bookCommentsRepository.findById(id);
    }

    @Transactional
    @Override
    public BookComments insert(String comment, String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        var bookComment = new BookComments(null, comment, book);
        return bookCommentsRepository.save(bookComment);
    }

    @Transactional
    @Override
    public BookComments update(String id, String comment) {
        BookComments currentComment = bookCommentsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Comment with id %s not found".formatted(id)));

        currentComment.setComment(comment);
        return bookCommentsRepository.save(currentComment);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        bookCommentsRepository.deleteById(id);
    }

}
