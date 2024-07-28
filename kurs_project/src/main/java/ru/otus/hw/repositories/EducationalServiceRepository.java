package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.hw.models.EducationalService;

public interface EducationalServiceRepository extends ReactiveMongoRepository<EducationalService, String> {
}
