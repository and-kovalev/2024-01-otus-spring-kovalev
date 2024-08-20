package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.EducationalSchedule;

import java.util.Date;

@Data
@AllArgsConstructor
public class EducationalScheduleDto {
    private String id;

    private EducationalServiceDto educationalService;

    private Date date;

    public static EducationalScheduleDto toDto(EducationalSchedule schedule) {
        return new EducationalScheduleDto(schedule.getId(),
                EducationalServiceDto.toDto(schedule.getEducationalService()),
                schedule.getDate());
    }

    public EducationalSchedule toDomainObject() {
        return new EducationalSchedule(id, educationalService.toDomainObject(), date);
    }
}
