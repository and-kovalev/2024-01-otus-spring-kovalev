package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.EducationalSchedule;

import java.util.Date;

public interface EducationalScheduleRepository extends ReactiveMongoRepository<EducationalSchedule, String> {

    Flux<EducationalSchedule> findByEducationalServiceId(String educationalServiceId);

    Mono<EducationalSchedule> findByEducationalServiceIdAndDate(String educationalServiceId, Date date);
}
