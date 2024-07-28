package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.repositories.PersonRepository;
import ru.otus.hw.repositories.TeacherRepository;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class RootController {

    private final PersonRepository personRepository;

    private final TeacherRepository teacherRepository;

    @GetMapping("/")
    public String mainPage(Model model) {
        return "main";
    }

    @GetMapping("/person/")
    public String mainPersonPage(Principal principal, Model model) {
        personRepository.findByFullName(principal.getName())
                .blockOptional()
                .ifPresent(person ->
                        model.addAttribute("child_id", person.getChilds().iterator().next().getId()));
        return "mainPerson";
    }

    @GetMapping("/teacher/")
    public String mainTeacherPage(Principal principal, Model model) {
        teacherRepository.findByFullName(principal.getName())
                .blockOptional()
                .ifPresent(teacher -> model.addAttribute("teacher_id", teacher.getId()));
        return "mainTeacher";
    }

}
