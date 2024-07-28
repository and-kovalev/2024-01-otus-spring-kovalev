package ru.otus.hw.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.TeachersSkillsDto;
import ru.otus.hw.models.TeachersSkills;
import ru.otus.hw.repositories.EducationalServiceRepository;
import ru.otus.hw.repositories.TeacherRepository;
import ru.otus.hw.repositories.TeachersSkillsRepository;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TeacherSkillsController {

    private final EducationalServiceRepository educationalServiceRepository;

    private final TeacherRepository teacherRepository;

    private final TeachersSkillsRepository teachersSkillsRepository;

    @CircuitBreaker(name = "addTeacherSkill", fallbackMethod = "unknownAddSkillsFallback")
    @PostMapping("/api/teacher/addSkill")
    public Mono<TeachersSkillsDto> addSkill(@RequestBody TeachersSkillsDto skill,
                                            @RequestParam("id") String teacherId) {
        return teacherRepository.findById(teacherId)
                .flatMap(teacher -> educationalServiceRepository.findById(skill.getEducationalService().getId())
                        .flatMap(edu -> teachersSkillsRepository
                                .findAllByTeacherIdAndEducationalServiceId(teacher.getId(), edu.getId())
                                        .flatMap(Mono::just)
                                        .switchIfEmpty(teachersSkillsRepository.insert(
                                                new TeachersSkills(null, edu, teacher)
                                                ))
                                                        .map(TeachersSkillsDto::toDto)));
    }

    @CircuitBreaker(name = "listSkills", fallbackMethod = "unknownSkillsFallback")
    @GetMapping("/api/teacher/listSkills")
    public Flux<TeachersSkillsDto> listForChild(@RequestParam("id") String teacherId) {
        return teachersSkillsRepository.findAllByTeacherId(teacherId)
                .map(TeachersSkillsDto::toDto);
    }

    @CircuitBreaker(name = "listAllTeachersSkills", fallbackMethod = "unknownSkillsFallback")
    @GetMapping("/api/person/listTechersSkills")
    public Flux<TeachersSkillsDto> listTechersSkills() {
        return teachersSkillsRepository.findAll()
                .map(TeachersSkillsDto::toDto);
    }

    public Mono<TeachersSkillsDto> unknownAddSkillsFallback(Exception ex) {
        log.error(ex.getMessage());
        return Mono.just(new TeachersSkillsDto(null, null,null));
    }

    public Flux<TeachersSkillsDto> unknownSkillsFallback(Exception ex) {
        log.error(ex.getMessage());
        return Flux.just(new TeachersSkillsDto(null, null,null));
    }

}
