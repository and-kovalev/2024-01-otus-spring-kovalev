package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Teacher;

@Data
@AllArgsConstructor
public class TeacherDto {
    private String id;

    private String fullName;

    private String telephone;

    public static TeacherDto toDto(Teacher teacher) {
        return new TeacherDto(teacher.getId(),
                teacher.getFullName(),
                teacher.getTelephone());
    }

    public Teacher toDomainObject() {
        return new Teacher(id, fullName, telephone);
    }
}
