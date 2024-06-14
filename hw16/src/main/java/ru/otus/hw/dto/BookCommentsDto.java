package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.otus.hw.models.BookComments;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class BookCommentsDto {
    private String id;

    private String comment;

    private BookDto book;

    public static BookCommentsDto toDto(BookComments bookComments) {
        return new BookCommentsDto(bookComments.getId(),
                bookComments.getComment(),
                BookDto.toDto(bookComments.getBook()));
    }

    public BookComments toDomainObject() {
        return new BookComments(id,
                comment,
                book.toDomainObject());
    }
}
