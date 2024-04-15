package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.BookComments;

@Component
public class BookCommentsConverter {
    public String commentToString(BookComments bookComments) {
        return "Book: %s, Comment: %s, Id: %s"
                .formatted(bookComments.getBook().getTitle(), bookComments.getComment(), bookComments.getId());
    }
}
