package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.service.TestRunnerService;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class Application {
    public static void main(String[] args) {

        /*ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();*/
        ApplicationContext context = SpringApplication.run(Application.class, args);
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();
    }
}