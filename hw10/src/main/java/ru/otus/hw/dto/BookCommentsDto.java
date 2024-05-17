package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.BookComments;

@Data
@AllArgsConstructor
public class BookCommentsDto {
    private long id;

    private String comment;

    private BookDto book;

    public static BookCommentsDto toDto(BookComments bookComments) {
        return new BookCommentsDto(bookComments.getId(),
                bookComments.getComment(),
                BookDto.toDto(bookComments.getBook()));
    }
}
