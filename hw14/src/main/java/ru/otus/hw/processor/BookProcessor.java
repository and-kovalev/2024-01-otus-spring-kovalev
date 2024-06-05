package ru.otus.hw.processor;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

@Component
@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<JpaBook, MongoBook> {

    private final BookMapper bookMapper;

    private final MongoAuthorRepository mongoAuthorRepository;

    private final MongoGenreRepository mongoGenreRepository;

    @Override
    public MongoBook process(@NotNull JpaBook book) {
        return bookMapper.mapBook(book,
                mongoAuthorRepository.findById(String.valueOf(book.getAuthor().getId())).get(),
                mongoGenreRepository.findById(String.valueOf(book.getGenre().getId())).get());
    }
}
