package ru.otus.hw.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookCommentsRepository;

@RequiredArgsConstructor
@Component
public class BooksCascadeMongoEventListener extends AbstractMongoEventListener<Book> {

    private final BookCommentsRepository bookCommentsRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        String bookId = String.valueOf(event.getSource().get("_id"));
        bookCommentsRepository.deleteByBookId(bookId);
    }
}
