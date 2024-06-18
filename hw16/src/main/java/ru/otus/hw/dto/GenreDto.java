package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Genre;

@Data
@AllArgsConstructor
public class GenreDto {
    private String id;

    private String name;

    public static GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public Genre toDomainObject() {
        return new Genre(id, name);
    }
}
