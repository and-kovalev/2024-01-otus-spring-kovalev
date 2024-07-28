package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.otus.hw.enums.UserType;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String name;

    private String pass;

    private UserType userType;
}
