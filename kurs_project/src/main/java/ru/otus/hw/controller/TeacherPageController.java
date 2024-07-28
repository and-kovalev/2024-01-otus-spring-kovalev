package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.exceptions.NotFoundException;

@Controller
@RequiredArgsConstructor
public class TeacherPageController {

    @GetMapping("/teacher/AddSkill/{id}")
    public String addSkill(@PathVariable(value = "id", required = false) String id, Model model) {
        model.addAttribute("teacher_id", id);
        return "addTeacherSkill.html";
    }

    @GetMapping("/teacher/listSkills/{id}")
    public String listSkills(@PathVariable(value = "id", required = false) String id, Model model) {
        model.addAttribute("teacher_id", id);
        return "listTeacherSkills.html";
    }

    @GetMapping("/teacher/listSchedule/{id}")
    public String listTeacherSchedule(@PathVariable(value = "id", required = false) String id, Model model) {
        model.addAttribute("teacher_id", id);
        return "listTeacherSchedule.html";
    }

    @GetMapping("/teacher/editSchedule/{id}")
    public String editTeacherSchedule(@PathVariable(value = "id", required = false) String id,
                               @RequestParam("teacher") String teacherId,
                               Model model) {
        model.addAttribute("schedule_id", id);
        model.addAttribute("teacher_id", teacherId);
        return "editTeacherEdu.html";
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.badRequest().body("Невено указан Url!");
    }
}
