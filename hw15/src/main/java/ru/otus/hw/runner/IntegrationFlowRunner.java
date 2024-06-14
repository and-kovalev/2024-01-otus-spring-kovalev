package ru.otus.hw.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.HeatingService;

@Component
@RequiredArgsConstructor
public class IntegrationFlowRunner implements CommandLineRunner {

    private final HeatingService heatingService;

    @Override
    public void run(String... args) {
        heatingService.startGenerateIceLoop();
    }

}
