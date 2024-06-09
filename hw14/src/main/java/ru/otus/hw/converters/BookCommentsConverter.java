package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.mongo.MongoBookComments;

@Component
public class BookCommentsConverter {
    public String commentToString(MongoBookComments bookComments) {
        return "JpaBook: %s, Comment: %s, Id: %s"
                .formatted(bookComments.getBook().getTitle(), bookComments.getComment(), bookComments.getId());
    }
}
