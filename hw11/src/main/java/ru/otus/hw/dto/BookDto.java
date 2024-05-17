package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.otus.hw.models.Book;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class BookDto {

    private String id;

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

    public Book toDomainObject() {
        return new Book(id,
                title,
                author.toDomainObject(),
                genre.toDomainObject());
    }

}
