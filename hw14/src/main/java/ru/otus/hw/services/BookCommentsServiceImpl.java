package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.mongo.MongoBookComments;
import ru.otus.hw.repositories.mongo.MongoBookCommentsRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookCommentsServiceImpl implements BookCommentsService {
    private final MongoBookCommentsRepository bookCommentsRepository;

    private final MongoBookRepository bookRepository;

    @Override
    public List<MongoBookComments> findAllForBook(String bookId) {
        return bookCommentsRepository.findAllByBookId(bookId);
    }

    @Override
    public Optional<MongoBookComments> findById(String id) {
        return bookCommentsRepository.findById(id);
    }

    @Override
    public MongoBookComments insert(String comment, String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("JpaBook with id %s not found".formatted(bookId)));
        var bookComment = new MongoBookComments(null, comment, book);
        return bookCommentsRepository.save(bookComment);
    }

    @Override
    public MongoBookComments update(String id, String comment) {
        MongoBookComments currentComment = bookCommentsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Comment with id %s not found".formatted(id)));

        currentComment.setComment(comment);
        return bookCommentsRepository.save(currentComment);
    }

    @Override
    public void deleteById(String id) {
        bookCommentsRepository.deleteById(id);
    }

}
