package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.otus.hw.models.jpa.JpaBookComments;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoBookComments;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookCommentMapper {

    @Mapping(target = "id", source = "comments.id")
    @Mapping(target = "book", source = "book")
    MongoBookComments mapComment(JpaBookComments comments, MongoBook book);

}
