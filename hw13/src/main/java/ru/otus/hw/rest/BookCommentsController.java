package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookCommentsDto;
import ru.otus.hw.services.BookCommentsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookCommentsController {

    private final BookCommentsService bookCommentsService;

    @GetMapping("/api/book_comments/{id}")
    public List<BookCommentsDto> listBookComments(@PathVariable(value = "id", required = false) long bookId) {
        return bookCommentsService.findAllForBook(bookId).stream().map(BookCommentsDto::toDto).toList();
    }

    @PostMapping("/api/book_comments/")
    public BookCommentsDto saveComment(@RequestBody BookCommentsDto bookCommentsDto) {
        var savedBook = bookCommentsService.save(bookCommentsDto);
        return BookCommentsDto.toDto(savedBook);
    }

    @PostMapping("/api/book_comments/deleteComment")
    public void deleteComment(@RequestBody BookCommentsDto bookCommentsDto) {
        bookCommentsService.deleteById(bookCommentsDto.getId());
    }
}
