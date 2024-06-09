package ru.otus.hw.processor;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.models.jpa.JpaAuthor;
import ru.otus.hw.models.mongo.MongoAuthor;

@Component
@RequiredArgsConstructor
public class AuthorProcessor implements ItemProcessor<JpaAuthor, MongoAuthor> {

    private final AuthorMapper authorMapper;

    @Override
    public MongoAuthor process(@NotNull JpaAuthor author) {
        return authorMapper.mapAuthor(author);
    }
}
