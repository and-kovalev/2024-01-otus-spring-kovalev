package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Teacher;

public interface TeacherRepository extends ReactiveMongoRepository<Teacher, String> {

    Mono<Teacher> findByTelephone(String telephone);

    Mono<Teacher> findByFullName(String fullName);
}
