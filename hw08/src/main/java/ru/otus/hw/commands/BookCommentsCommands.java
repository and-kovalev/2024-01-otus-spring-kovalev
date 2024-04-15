package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookCommentsConverter;
import ru.otus.hw.services.BookCommentsService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class BookCommentsCommands {

    private final BookCommentsService bookCommentsService;

    private final BookCommentsConverter bookCommentsConverter;

    @ShellMethod(value = "Find all Comments for book", key = "ca")
    public String findAllForBook(String bookId) {
        return bookCommentsService.findAllForBook(bookId).stream()
                .map(bookCommentsConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find Comments by id", key = "cbid")
    public String findCommentById(String id) {
        return bookCommentsService.findById(id)
                .map(bookCommentsConverter::commentToString)
                .orElse("Author with id %s not found".formatted(id));
    }

    @ShellMethod(value = "Insert book comment", key = "cins")
    public String insertComment(String comment, String bookId) {
        var savedBook = bookCommentsService.insert(comment, bookId);
        return bookCommentsConverter.commentToString(savedBook);
    }

    @ShellMethod(value = "Update book comment", key = "cupd")
    public String updateComment(String id, String comment, String bookId) {
        var savedBook = bookCommentsService.update(id, comment);
        return bookCommentsConverter.commentToString(savedBook);
    }

    @ShellMethod(value = "Delete book comment by id", key = "cdel")
    public void deleteComment(String id) {
        bookCommentsService.deleteById(id);
    }
}
