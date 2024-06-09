package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.mongo.MongoGenre;

@Component
public class GenreConverter {
    public String genreToString(MongoGenre genre) {
        return "Id: %s, Name: %s".formatted(genre.getId(), genre.getName());
    }
}
