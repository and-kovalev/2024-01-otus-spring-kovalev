package ru.otus.hw.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.ChildScheduleDto;
import ru.otus.hw.dto.EducationalScheduleDto;
import ru.otus.hw.models.ChildSchedule;
import ru.otus.hw.models.EducationalSchedule;
import ru.otus.hw.repositories.ChildRepository;
import ru.otus.hw.repositories.ChildScheduleRepository;
import ru.otus.hw.repositories.EducationalScheduleRepository;
import ru.otus.hw.repositories.EducationalServiceRepository;
import ru.otus.hw.repositories.TeachersSkillsRepository;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EducationalScheduleController {

    private final EducationalScheduleRepository educationalScheduleRepository;

    private final EducationalServiceRepository educationalServiceRepository;

    private final ChildRepository childRepository;

    private final ChildScheduleRepository childScheduleRepository;

    private final TeachersSkillsRepository teachersSkillsRepository;

    @CircuitBreaker(name = "saveSchedule", fallbackMethod = "unknownSaveScheduleFallback")
    @PostMapping("/api/schedule/child")
    public Mono<ChildScheduleDto> saveSchedule(@RequestBody EducationalScheduleDto schedule,
                                               @RequestParam("id") String childId) {
        return educationalServiceRepository.findById(schedule.getEducationalService().getId())
                .flatMap(edu ->
                        educationalScheduleRepository.findByEducationalServiceIdAndDate(edu.getId(), schedule.getDate())
                                .flatMap(Mono::just)
                                .switchIfEmpty(educationalScheduleRepository.insert(
                                        new EducationalSchedule(null, edu, schedule.getDate())))
                                .flatMap(sch ->
                                        childRepository.findById(childId)
                                                .flatMap(child ->
                                                        childScheduleRepository
                                                                .findAllByChildIdAndEducationalScheduleId(
                                                                        child.getId(), sch.getId())
                                                                .flatMap(Mono::just)
                                                                .switchIfEmpty(childScheduleRepository.insert(
                                                                        new ChildSchedule(null, sch, child))))
                                )
                                .map(ChildScheduleDto::toDto));
    }

    @CircuitBreaker(name = "listForChild", fallbackMethod = "unknownListForChildFallback")
    @GetMapping("/api/schedule/listForChild")
    public Flux<ChildScheduleDto> listForChild(@RequestParam("id") String childId) {
        return childScheduleRepository.findAllByChildId(childId)
                .map(ChildScheduleDto::toDto);
    }

    @CircuitBreaker(name = "saveSchedule", fallbackMethod = "unknownSaveScheduleFallback")
    @GetMapping("/api/schedule/child/find")
    public Mono<ChildScheduleDto> findForChild(@RequestParam("id") String scheduleId) {
        return childScheduleRepository.findById(scheduleId)
                .map(ChildScheduleDto::toDto);
    }

    @PostMapping("/api/schedule/child/delete")
    public Mono<Void> deleteForChild(@RequestParam("id") String scheduleId) {
        return childScheduleRepository.deleteById(scheduleId);
    }

    @PostMapping("/api/schedule/child/edit")
    public Mono<EducationalScheduleDto> editForChild(@RequestBody ChildScheduleDto schedule) {
        return childScheduleRepository.findById(schedule.getId())
                .flatMap(chs -> {
                    var sch = chs.getEducationalSchedule();
                    return educationalScheduleRepository.save(sch
                            .setDate(getLocalTime(schedule.getSchedule().getDate())));
                })
                .map(EducationalScheduleDto::toDto);
    }

    @CircuitBreaker(name = "listForTeacher", fallbackMethod = "unknownListEduFallback")
    @GetMapping("/api/schedule/listForTeacher")
    public Flux<EducationalScheduleDto> listForTeacher(@RequestParam("id") String teacherId) {
        return teachersSkillsRepository.findAllByTeacherId(teacherId)
                .flatMap(teachersSkills -> educationalScheduleRepository
                        .findByEducationalServiceId(teachersSkills.getEducationalService().getId())
                        .map(EducationalScheduleDto::toDto));
    }

    @CircuitBreaker(name = "listForTeacher", fallbackMethod = "unknownSingleEduFallback")
    @GetMapping("/api/schedule/teacher/find")
    public Mono<EducationalScheduleDto> findForTeacher(@RequestParam("id") String scheduleId) {
        return educationalScheduleRepository.findById(scheduleId)
                .map(EducationalScheduleDto::toDto);
    }

    @CircuitBreaker(name = "listForTeacher", fallbackMethod = "unknownSingleEduFallback")
    @PostMapping("/api/schedule/teacher/edit")
    public Mono<EducationalScheduleDto> editForTeacher(@RequestBody ChildScheduleDto schedule) {
        return educationalScheduleRepository.findById(schedule.getId())
                .flatMap(chs -> educationalScheduleRepository
                        .save(chs.setDate(getLocalTime(schedule.getSchedule().getDate()))))
                .map(EducationalScheduleDto::toDto);
    }

    private static Date getLocalTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MILLISECOND, -TimeZone.getDefault().getRawOffset());
        return cal.getTime();
    }

    public Mono<ChildScheduleDto> unknownSaveScheduleFallback(Exception ex) {
        log.error(ex.getMessage());
        return Mono.just(new ChildScheduleDto(null, null,null));
    }

    public Flux<ChildScheduleDto> unknownListForChildFallback(Exception ex) {
        log.error(ex.getMessage());
        return Flux.just(new ChildScheduleDto(null, null,null));
    }

    public Mono<EducationalScheduleDto> unknownSingleEduFallback(Exception ex) {
        log.error(ex.getMessage());
        return Mono.just(new EducationalScheduleDto(null, null,null));
    }

    public Flux<EducationalScheduleDto> unknownListEduFallback(Exception ex) {
        log.error(ex.getMessage());
        return Flux.just(new EducationalScheduleDto(null, null,null));
    }

}
