package ru.otus.hw.processor;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.mapper.BookCommentMapper;
import ru.otus.hw.models.jpa.JpaBookComments;
import ru.otus.hw.models.mongo.MongoBookComments;
import ru.otus.hw.repositories.mongo.MongoBookRepository;

@Component
@RequiredArgsConstructor
public class BookCommentsProcessor implements ItemProcessor<JpaBookComments, MongoBookComments> {

    private final BookCommentMapper bookCommentMapper;

    private final MongoBookRepository mongoBookRepository;

    @Override
    public MongoBookComments process(@NotNull JpaBookComments bookComments) {
        return bookCommentMapper.mapComment(bookComments, mongoBookRepository
                .findById(String.valueOf(bookComments.getBook().getId())).get());
    }
}
