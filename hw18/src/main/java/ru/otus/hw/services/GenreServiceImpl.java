package ru.otus.hw.services;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    @RateLimiter(name = "findAllGenre")
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    @RateLimiter(name = "findByIdGenre")
    public Optional<Genre> findById(long id) {
        return genreRepository.findById(id);
    }
}
