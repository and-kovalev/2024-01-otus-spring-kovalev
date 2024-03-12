package ru.otus.hw.shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.shell.CommandNotFound;
import org.springframework.shell.InputProvider;
import org.springframework.shell.ResultHandlerService;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.service.TestRunnerService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@DisplayName("Тест команд shell ")
@SpringBootTest
class ApplicationShellCommandsTest {

    private static final String TESTING_PATTERN = "Testing done";
    private static final String COMMAND_TEST = "test";
    private static final String COMMAND_TEST_SHORT = "t";
    private static final String BAD_COMMAND = "Ivan";

    private InputProvider inputProvider;

    private ArgumentCaptor<Object> argumentCaptor;

    @MockBean
    private TestRunnerService testRunnerService;

    @SpyBean
    private ResultHandlerService resultHandlerService;

    @Autowired
    private Shell shell;

    @BeforeEach
    void setUp() {
        inputProvider = mock(InputProvider.class);
        argumentCaptor = ArgumentCaptor.forClass(Object.class);
    }

    @DisplayName(" должен попращаться после выполнения команды")
    @Test
    void shouldReturnExpectedFarewellAfterCommandEvaluated() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> COMMAND_TEST)
                .thenReturn(null);

        shell.run(inputProvider);
        verify(resultHandlerService, times(1)).handle(argumentCaptor.capture());
        List<Object> results = argumentCaptor.getAllValues();
        assertThat(results).contains(TESTING_PATTERN);
    }

    @DisplayName(" должен попращаться одинаково после выполнения всех команд")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnExpectedFarewellAfterTwoCommandEvaluated() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> COMMAND_TEST)
                .thenReturn(() -> COMMAND_TEST_SHORT)
                .thenReturn(null);

        shell.run(inputProvider);
        verify(resultHandlerService, times(2)).handle(argumentCaptor.capture());
        List<Object> results = argumentCaptor.getAllValues();
        assertThat(results).containsExactlyInAnyOrder(TESTING_PATTERN,TESTING_PATTERN);
    }

    @DisplayName(" должен возвращать CommandNotFound при попытке выполнения неизвестной команды")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnExpectAfterCommandNotFound() {
        when(inputProvider.readInput())
                .thenReturn(() -> BAD_COMMAND)
                .thenReturn(null);

        assertThatCode(() -> shell.run(inputProvider)).isInstanceOf(CommandNotFound.class);
    }
}