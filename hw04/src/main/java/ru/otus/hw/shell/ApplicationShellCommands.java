package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent(value = "Application Shell Commands")
@RequiredArgsConstructor
public class ApplicationShellCommands {

    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Run Test command", key = {"t", "test"})
    public String runTest() {
        testRunnerService.run();
        return "Testing done";
    }

}
