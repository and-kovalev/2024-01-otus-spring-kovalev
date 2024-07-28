package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.ChildSchedule;

@Data
@AllArgsConstructor
public class ChildScheduleDto {
    private String id;

    private EducationalScheduleDto schedule;

    private ChildDto child;

    public static ChildScheduleDto toDto(ChildSchedule schedule) {
        return new ChildScheduleDto(schedule.getId(),
                EducationalScheduleDto.toDto(schedule.getEducationalSchedule()),
                ChildDto.toDto(schedule.getChild()));
    }

    public ChildSchedule toDomainObject() {
        return new ChildSchedule(id, schedule.toDomainObject(), child.toDomainObject());
    }
}
