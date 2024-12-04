package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Book;

@Data
@AllArgsConstructor
public class BookDto {

    private long id;

    private String title;

    private AuthorDto author;

    private GenreDto genre;

    public static BookDto toDto(Book book) {
        return new BookDto(book.getId(),
                book.getTitle(),
                AuthorDto.toDto(book.getAuthor()),
                GenreDto.toDto(book.getGenre())
        );
    }
}
