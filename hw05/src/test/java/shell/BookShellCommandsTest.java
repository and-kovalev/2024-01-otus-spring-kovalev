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
import ru.otus.hw.commands.BookCommands;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@DisplayName("Тест команд для книг ")
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BookShellCommandsTest {

    private static final String COMMAND_FIND_ALL = "ab";
    private static final String COMMAND_FIND_BY_ID = "bbid";
    private static final String COMMAND_INSERT = "bins";
    private static final String COMMAND_UPDATE = "bupd";
    private static final String COMMAND_DELETE = "bdel";
    private static final String BOOK_ID = "1";
    private static final String BOOK_TITLE = "book_1";
    private static final String AUTHOR_ID = "1";
    private static final String GENRE_ID = "1";
    private static final String BAD_COMMAND = "Ivan";

    private InputProvider inputProvider;

    private ArgumentCaptor<Object> argumentCaptor;

    @MockBean
    private BookCommands bookCommands;

    @SpyBean
    private ResultHandlerService resultHandlerService;

    @Autowired
    private Shell shell;

    @BeforeEach
    void setUp() {
        inputProvider = mock(InputProvider.class);
        argumentCaptor = ArgumentCaptor.forClass(Object.class);
    }

    @DisplayName(" должен запросить список все книги")
    @Test
    void shouldExecFindAllCommand() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> COMMAND_FIND_ALL)
                .thenReturn(null);

        shell.run(inputProvider);
        verify(resultHandlerService).handle(argumentCaptor.capture());
        verify(bookCommands).findAllBooks();
    }

    @DisplayName(" должен запросить книгу по id")
    @Test
    void shouldExecFindByIdCommand() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> String.format("%s %s", COMMAND_FIND_BY_ID, BOOK_ID))
                .thenReturn(null);

        shell.run(inputProvider);
        verify(resultHandlerService).handle(argumentCaptor.capture());
        verify(bookCommands).findBookById(eq(Long.valueOf(BOOK_ID)));
    }

    @DisplayName(" должен вставить книгу")
    @Test
    void shouldExecInsertCommand() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> String.format("%s %s %s %s", COMMAND_INSERT, BOOK_TITLE, AUTHOR_ID, GENRE_ID))
                .thenReturn(null);

        shell.run(inputProvider);
        verify(resultHandlerService).handle(argumentCaptor.capture());
        verify(bookCommands).insertBook(eq(BOOK_TITLE), eq(Long.valueOf(AUTHOR_ID)), eq(Long.valueOf(GENRE_ID)));
    }

    @DisplayName(" должен обновить книгу")
    @Test
    void shouldExecUpdateCommand() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> String.format("%s %s %s %s %s", COMMAND_UPDATE, BOOK_ID, BOOK_TITLE, AUTHOR_ID, GENRE_ID))
                .thenReturn(null);

        shell.run(inputProvider);
        verify(resultHandlerService).handle(argumentCaptor.capture());
        verify(bookCommands).updateBook(
                eq(Long.valueOf(BOOK_ID)),
                eq(BOOK_TITLE),
                eq(Long.valueOf(AUTHOR_ID)),
                eq(Long.valueOf(GENRE_ID))
        );
    }

    @DisplayName(" должен удалить книгу по id")
    @Test
    void shouldExecDeleteCommand() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> String.format("%s %s", COMMAND_DELETE, BOOK_ID))
                .thenReturn(null);

        shell.run(inputProvider);
        verify(resultHandlerService).handle(argumentCaptor.capture());
        verify(bookCommands).deleteBook(eq(Long.valueOf(BOOK_ID)));
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