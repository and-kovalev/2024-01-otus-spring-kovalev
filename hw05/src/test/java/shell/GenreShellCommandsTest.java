package shell;

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
import ru.otus.hw.Application;
import ru.otus.hw.commands.GenreCommands;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@DisplayName("Тест команд для жанров ")
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class GenreShellCommandsTest {

    private static final String COMMAND_FIND_ALL = "ag";
    private static final String COMMAND_FIND_BY_ID = "gbid";
    private static final String GENRE_ID = "1";
    private static final String BAD_COMMAND = "Ivan";

    private InputProvider inputProvider;

    private ArgumentCaptor<Object> argumentCaptor;

    @MockBean
    private GenreCommands genreCommands;

    @SpyBean
    private ResultHandlerService resultHandlerService;

    @Autowired
    private Shell shell;

    @BeforeEach
    void setUp() {
        inputProvider = mock(InputProvider.class);
        argumentCaptor = ArgumentCaptor.forClass(Object.class);
    }

    @DisplayName(" должен запросить список всех жанров")
    @Test
    void shouldExecFindAllCommand() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> COMMAND_FIND_ALL)
                .thenReturn(null);

        shell.run(inputProvider);
        verify(resultHandlerService).handle(argumentCaptor.capture());
        verify(genreCommands).findAllGenres();
    }

    @DisplayName(" должен запросить жанр по id")
    @Test
    void shouldExecFindByIdCommand() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> String.format("%s %s",COMMAND_FIND_BY_ID, GENRE_ID))
                .thenReturn(null);

        shell.run(inputProvider);
        verify(resultHandlerService).handle(argumentCaptor.capture());
        verify(genreCommands).findGenreById(eq(Long.valueOf(GENRE_ID)));
    }

    @DisplayName(" должен возвращать CommandNotFound при попытке выполнения неизвестной команды")
    @Test
    void shouldReturnExpectAfterCommandNotFound() {
        when(inputProvider.readInput())
                .thenReturn(() -> BAD_COMMAND)
                .thenReturn(null);

        assertThatCode(() -> shell.run(inputProvider)).isInstanceOf(CommandNotFound.class);
    }
}