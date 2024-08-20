package ru.otus.hw.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.EducationalServiceDto;
import ru.otus.hw.repositories.EducationalServiceRepository;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EducationalServiceController {

    private final EducationalServiceRepository educationalServiceRepository;

    @CircuitBreaker(name = "listServices", fallbackMethod = "unknownListEduFallback")
    @GetMapping("/api/edu/")
    public Flux<EducationalServiceDto> listServices() {
        return educationalServiceRepository.findAll().map(EducationalServiceDto::toDto);
    }

    @CircuitBreaker(name = "findServices", fallbackMethod = "unknownFindEduFallback")
    @GetMapping("/api/edu/find")
    public Mono<EducationalServiceDto> findServices(@RequestParam("id") String eduId) {
        return educationalServiceRepository.findById(eduId).map(EducationalServiceDto::toDto);
    }

    public Mono<EducationalServiceDto> unknownFindEduFallback(Exception ex) {
        log.error(ex.getMessage());
        return Mono.just(new EducationalServiceDto(null, null,false));
    }

    public Flux<EducationalServiceDto> unknownListEduFallback(Exception ex) {
        log.error(ex.getMessage());
        return Flux.just(new EducationalServiceDto(null, null,false));
    }
}
