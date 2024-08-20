package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.ChildSchedule;

public interface ChildScheduleRepository extends ReactiveMongoRepository<ChildSchedule, String> {

    Flux<ChildSchedule> findAllByChildId(String childId);

    Flux<ChildSchedule> findAllByEducationalScheduleId(String educationalScheduleId);

    Mono<ChildSchedule> findAllByChildIdAndEducationalScheduleId(String childId, String educationalScheduleId);
}
