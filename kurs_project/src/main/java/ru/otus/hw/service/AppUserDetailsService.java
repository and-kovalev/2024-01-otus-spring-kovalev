package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.enums.UserType;
import ru.otus.hw.exceptions.UserNotLoginException;
import ru.otus.hw.repositories.PersonRepository;
import ru.otus.hw.repositories.TeacherRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    private final TeacherRepository teacherRepository;

    private AppUserPrincipal appUserPrincipal;

    @Override
    public UserDetails loadUserByUsername(String username) {
        this.appUserPrincipal = null;

        personRepository.findByFullName(username)
                .blockOptional()
                .ifPresent(person -> {
                    this.appUserPrincipal =
                            getAppUserPrincipal(person.getFullName(), person.getTelephone(), UserType.PERSON);
                });

        if (Objects.isNull(this.appUserPrincipal)) {
            teacherRepository.findByFullName(username)
                    .blockOptional()
                    .ifPresent(teacher -> {
                        this.appUserPrincipal =
                                getAppUserPrincipal(teacher.getFullName(), teacher.getTelephone(), UserType.TEACHER);
                    });
        }

        if (Objects.isNull(this.appUserPrincipal)) {
            throw new UserNotLoginException();
        }

        return this.appUserPrincipal;
    }

    private AppUserPrincipal getAppUserPrincipal(String name,
                                                 String pass,
                                                 UserType userType) {
        return new AppUserPrincipal(
                new UserDto()
                        .setName(name)
                        .setPass(pass)
                        .setUserType(userType),
                new BCryptPasswordEncoder());
    }
}