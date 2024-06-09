package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.otus.hw.models.jpa.JpaAuthor;
import ru.otus.hw.models.mongo.MongoAuthor;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorMapper {

    MongoAuthor mapAuthor(JpaAuthor authorSrc);
}
