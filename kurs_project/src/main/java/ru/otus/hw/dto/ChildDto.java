package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Child;

@Data
@AllArgsConstructor
public class ChildDto {
    private String id;

    private String fullName;

    private String telephone;

    public static ChildDto toDto(Child child) {
        return new ChildDto(child.getId(),
                child.getFullName(),
                child.getTelephone());
    }

    public Child toDomainObject() {
        return new Child(id, fullName, telephone, null);
    }
}
