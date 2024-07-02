package ru.otus.hw.controller;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RootController {

    @GetMapping("/")
    @RateLimiter(name = "mainPage")
    public String mainPage(Model model) {
        return "main";
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<String> handleTooManyRequest(RequestNotPermitted ex) {
        return ResponseEntity.badRequest().body("Too Many Requests - Retry After 1 Minute");
    }
}
