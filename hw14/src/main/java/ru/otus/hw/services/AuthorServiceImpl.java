package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final MongoAuthorRepository mongoAuthorRepository;

    @Override
    public List<MongoAuthor> findAll() {
        return mongoAuthorRepository.findAll();
    }

    @Override
    public Optional<MongoAuthor> findById(String id) {
        return mongoAuthorRepository.findById(id);
    }
}
