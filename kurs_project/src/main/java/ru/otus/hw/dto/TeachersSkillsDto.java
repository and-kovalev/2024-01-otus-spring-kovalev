package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.TeachersSkills;

@Data
@AllArgsConstructor
public class TeachersSkillsDto {
    private String id;

    private EducationalServiceDto educationalService;

    private TeacherDto teacher;

    public static TeachersSkillsDto toDto(TeachersSkills teachersSkills) {
        return new TeachersSkillsDto(teachersSkills.getId(),
                EducationalServiceDto.toDto(teachersSkills.getEducationalService()),
                TeacherDto.toDto(teachersSkills.getTeacher()));
    }

    public TeachersSkills toDomainObject() {
        return new TeachersSkills(id, educationalService.toDomainObject(), teacher.toDomainObject());
    }
}
