package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/api/authors/")
    public List<AuthorDto> listAuthors() {
        return authorService.findAll().stream().map(AuthorDto::toDto).toList();
    }

    @PostAuthorize("hasRole('USER')")
    @PostMapping("/api/authors/")
    public AuthorDto saveAuthor(@RequestBody AuthorDto authorDto) {
        var savedAuthor = authorService.save(authorDto.toDomainObject());
        return AuthorDto.toDto(savedAuthor);
    }
}
