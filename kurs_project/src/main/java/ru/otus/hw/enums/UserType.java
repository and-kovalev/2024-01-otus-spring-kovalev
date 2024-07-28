package ru.otus.hw.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UserType {
    PERSON("ROLE_USER"),
    TEACHER("ROLE_TEACHER"),
    ADMIN("ROLE_ADMIN");

    @Getter
    private String role;
}
