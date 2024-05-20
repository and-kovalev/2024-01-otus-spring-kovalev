package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
@AllArgsConstructor
@Document
public class Book {
    @Id
    private String id;

    private String title;

    private Author author;

    private Genre genre;
}