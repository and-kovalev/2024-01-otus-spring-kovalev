package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final MongoGenreRepository mongoGenreRepository;

    @Override
    public List<MongoGenre> findAll() {
        return mongoGenreRepository.findAll();
    }

    @Override
    public Optional<MongoGenre> findById(String id) {
        return mongoGenreRepository.findById(id);
    }
}
