package ru.otus.hw.services;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    @RateLimiter(name = "findByIdBook")
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    @RateLimiter(name = "findAllBook")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    @RateLimiter(name = "insertDBook")
    public Book insert(String title, long authorId, long genreId) {
        return save(0, title, authorId, genreId);
    }

    @Transactional
    @Override
    @RateLimiter(name = "insertBook")
    public Book insert(Book book) {
        return save(0, book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
    }

    @Transactional
    @Override
    @RateLimiter(name = "updateBook")
    public Book update(long id, String title, long authorId, long genreId) {
        return save(id, title, authorId, genreId);
    }

    @Transactional
    @Override
    @RateLimiter(name = "deleteByIdBook")
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    @Override
    @RateLimiter(name = "saveBook")
    public Book save(BookDto bookDto) {
        var book = bookRepository.findById(bookDto.getId())
                .orElse(new Book().setId(bookDto.getId()));

        book.setTitle(bookDto.getTitle())
                .setAuthor(authorRepository.findById(bookDto.getAuthor().getId()).orElse(null))
                .setGenre(genreRepository.findById(bookDto.getGenre().getId()).orElse(null));

        return bookRepository.save(book);
    }

    private Book save(long id, String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        var book = new Book(id, title, author, genre, null);
        return bookRepository.save(book);
    }
}
