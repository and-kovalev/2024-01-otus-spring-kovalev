package ru.otus.hw.processor;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.models.jpa.JpaGenre;
import ru.otus.hw.models.mongo.MongoGenre;

@Component
@RequiredArgsConstructor
public class GenreProcessor implements ItemProcessor<JpaGenre, MongoGenre> {

    private final GenreMapper genreMapper;

    @Override
    public MongoGenre process(@NotNull JpaGenre genre) {
        return genreMapper.mapGenre(genre);
    }
}
