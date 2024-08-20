package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.TeachersSkills;

public interface TeachersSkillsRepository extends ReactiveMongoRepository<TeachersSkills, String> {

    Flux<TeachersSkills> findAllByTeacherId(String teacherId);

    Mono<TeachersSkills> findAllByTeacherIdAndEducationalServiceId(String teacherId, String educationalScheduleId);
}
