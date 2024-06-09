package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final MongoAuthorRepository authorRepository;

    private final MongoGenreRepository genreRepository;

    private final MongoBookRepository bookRepository;

    @Override
    public Optional<MongoBook> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<MongoBook> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public MongoBook insert(String title, String authorId, String genreId) {
        return save(null, title, authorId, genreId);
    }

    @Transactional
    @Override
    public MongoBook update(String id, String title, String authorId, String genreId) {
        return save(id, title, authorId, genreId);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    private MongoBook save(String id, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("MongoAuthor with id %s not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("MongoGenre with id %s not found".formatted(genreId)));
        var book = new MongoBook(id, title, author, genre);
        return bookRepository.save(book);
    }
}
