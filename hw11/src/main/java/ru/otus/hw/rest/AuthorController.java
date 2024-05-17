package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    @GetMapping("/api/authors/")
    public Flux<AuthorDto> listAuthors() {
        return authorRepository.findAll().map(AuthorDto::toDto);
    }

    @GetMapping("/api/authors/find")
    public Mono<AuthorDto> findAuthors(@RequestParam("id") String id) {
        return authorRepository.findById(id).map(AuthorDto::toDto);
    }

    @PostMapping("/api/authors/editAuthor")
    public Mono<AuthorDto> saveAuthor(@RequestBody AuthorDto authorDto) {
        return authorRepository.save(authorDto.toDomainObject()).map(AuthorDto::toDto);
    }
}
