package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Child;
import ru.otus.hw.models.EducationalSchedule;

public interface ChildRepository extends ReactiveMongoRepository<Child, String> {

    Mono<Child> findByTelephone(String telephone);

    Flux<EducationalSchedule> findAllSchedulesById(String id);
}
