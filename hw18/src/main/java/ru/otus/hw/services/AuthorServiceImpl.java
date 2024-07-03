package ru.otus.hw.services;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    @RateLimiter(name = "findAllAuthors")
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    @RateLimiter(name = "findByIdAuthors")
    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }

    @Override
    @RateLimiter(name = "saveAuthors")
    public Author save(Author author) {
        return authorRepository.save(author);
    }
}