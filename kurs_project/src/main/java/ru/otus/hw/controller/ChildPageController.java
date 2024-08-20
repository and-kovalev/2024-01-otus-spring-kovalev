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
public class ChildPageController {

    @GetMapping("/child/AddService/{id}")
    public String addChildSchedule(@PathVariable(value = "id", required = false) String id, Model model) {
        model.addAttribute("child_id", id);
        return "addChildEdu.html";
    }

    @GetMapping("/child/listSchedule/{id}")
    public String listSchedule(@PathVariable(value = "id", required = false) String id, Model model) {
        model.addAttribute("child_id", id);
        return "listChildsSchedule.html";
    }

    @GetMapping("/child/editSchedule/{id}")
    public String editSchedule(@PathVariable(value = "id", required = false) String id,
                               @RequestParam("child") String childId,
                               Model model) {
        model.addAttribute("schedule_id", id);
        model.addAttribute("child_id", childId);
        return "editChildEdu.html";
    }

    @GetMapping("/child/DelService/{id}")
    public String deleteChildSchedule(@PathVariable(value = "id", required = false) String id,
                                      @RequestParam("child") String childId,
                                      Model model) {
        model.addAttribute("schedule_id", id);
        model.addAttribute("child_id", childId);
        return "delChildEdu.html";
    }

    @GetMapping("/child/ListTeachers")
    public String listTeachers() {
        return "listAllTeachersSkills.html";
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.badRequest().body("Невено указан Url!");
    }
}
