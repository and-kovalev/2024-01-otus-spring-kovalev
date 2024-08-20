package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.exceptions.NotFoundException;

@Controller
@RequiredArgsConstructor
public class RegistrationPageController {

    @GetMapping("/reg/Person")
    public String regPerson() {
        return "regPerson.html";
    }

    @GetMapping("/reg/Teacher")
    public String regTeacher() {
        return "regTeacher.html";
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.badRequest().body("Невено указан Url!");
    }
}
