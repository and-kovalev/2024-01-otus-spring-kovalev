package ru.otus.hw.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.ChildDto;
import ru.otus.hw.dto.RegistrationPersonDto;
import ru.otus.hw.dto.RegistrationTeacherDto;
import ru.otus.hw.dto.TeacherDto;
import ru.otus.hw.models.Child;
import ru.otus.hw.models.Person;
import ru.otus.hw.models.Teacher;
import ru.otus.hw.repositories.ChildRepository;
import ru.otus.hw.repositories.PersonRepository;
import ru.otus.hw.repositories.TeacherRepository;

import java.util.HashSet;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final ChildRepository childRepository;

    private final PersonRepository personRepository;

    private final TeacherRepository teacherRepository;

    @CircuitBreaker(name = "saveRegPerson", fallbackMethod = "unknownRegPersonFallback")
    @PostMapping("/api/reg/Person")
    public Mono<ChildDto> saveRegPerson(@NotNull @Validated @RequestBody RegistrationPersonDto registration) {
        return Mono.zip(
                        childRepository.findByTelephone(registration.getChildTelephone())
                                .flatMap(Mono::just)
                                .switchIfEmpty(childRepository.insert(
                                        new Child(null, registration.getChildFio(),
                                                registration.getChildTelephone(),
                                                new HashSet<>()))),
                        personRepository.findByTelephone(registration.getTelephone())
                                .flatMap(Mono::just)
                                .switchIfEmpty(personRepository.insert(
                                        new Person(null, registration.getFio(),
                                                registration.getTelephone(),
                                                new HashSet<>())))
                )
                .flatMap(t -> {
                    var child = t.getT1();
                    var person = t.getT2();
                    child.getPersonsForChild().add(person);
                    person.getChilds().add(child);
                    return personRepository.save(person)
                            .flatMap(pr -> childRepository.save(child))
                            .map(ChildDto::toDto);
                });
    }

    @CircuitBreaker(name = "saveRegTeacher", fallbackMethod = "unknownRegTeacherFallback")
    @PostMapping("/api/reg/Teacher")
    public Mono<TeacherDto> saveRegTeacher(@NotNull @Validated @RequestBody RegistrationTeacherDto registration) {
        return teacherRepository.findByTelephone(registration.getTelephone())
                .flatMap(Mono::just)
                .switchIfEmpty(teacherRepository.insert(
                        new Teacher(null,
                                registration.getFio(),
                                registration.getTelephone())
                ))
                .map(TeacherDto::toDto);
    }

    public Mono<ChildDto> unknownRegPersonFallback(Exception ex) {
        log.error(ex.getMessage());
        return Mono.just(new ChildDto(null, null, null));
    }

    public Mono<TeacherDto> unknownRegTeacherFallback(Exception ex) {
        log.error(ex.getMessage());
        return Mono.just(new TeacherDto(null, null, null));
    }
}
