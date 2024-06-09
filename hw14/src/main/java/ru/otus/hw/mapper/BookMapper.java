package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoGenre;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    @Mapping(target = "id", source = "bookSrc.id")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "genre", source = "genre")
    MongoBook mapBook(JpaBook bookSrc, MongoAuthor author, MongoGenre genre);
}
