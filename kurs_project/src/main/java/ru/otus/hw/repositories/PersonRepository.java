package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Person;

public interface PersonRepository extends ReactiveMongoRepository<Person, String> {

    Mono<Person> findByTelephone(String telephone);

    Mono<Person> findByFullName(String fullName);
}
