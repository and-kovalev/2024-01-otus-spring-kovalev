package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.EducationalService;

@Data
@AllArgsConstructor
public class EducationalServiceDto {
    private String id;

    private String service;

    private boolean isIndividual;

    public static EducationalServiceDto toDto(EducationalService edu) {
        return new EducationalServiceDto(edu.getId(),
                edu.getService(),
                edu.isIndividual());
    }

    public EducationalService toDomainObject() {
        return new EducationalService(id, service, isIndividual);
    }
}
