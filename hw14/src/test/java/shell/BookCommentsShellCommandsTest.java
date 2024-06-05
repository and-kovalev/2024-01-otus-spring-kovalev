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
import ru.otus.hw.commands.BookCommentsCommands;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoBookComments;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.services.BookCommentsService;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@DisplayName("Тест команд для комментариев книг ")
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BookCommentsShellCommandsTest {

    private static final String COMMAND_FIND_ALL = "ca";
    private static final String COMMAND_FIND_BY_ID = "cbid";
    private static final String COMMAND_INSERT = "cins";
    private static final String COMMAND_UPDATE = "cupd";
    private static final String COMMAND_DELETE = "cdel";
    private static final String BOOK_COMMENT_ID = "1";
    private static final String BOOK_COMMENT = "comment_1";
    private static final String AUTHOR_ID = "1";
    private static final String GENRE_ID = "1";
    private static final String BAD_COMMAND = "Ivan";

    private InputProvider inputProvider;

    private ArgumentCaptor<Object> argumentCaptor;

    @Autowired
    private BookCommentsCommands bookCommentsCommands;

    @MockBean
    private BookCommentsService bookCommentsService;

    @SpyBean
    private ResultHandlerService resultHandlerService;

    @Autowired
    private Shell shell;

    @BeforeEach
    void setUp() {
        inputProvider = mock(InputProvider.class);
        argumentCaptor = ArgumentCaptor.forClass(Object.class);
    }

    @DisplayName(" должен запросить список комментариев книги")
    @Test
    void shouldExecFindAllCommand() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> String.format("%s %s", COMMAND_FIND_ALL, BOOK_COMMENT_ID))
                .thenReturn(null);

        shell.run(inputProvider);
        verify(resultHandlerService).handle(argumentCaptor.capture());
        verify(bookCommentsService).findAllForBook(BOOK_COMMENT_ID);
    }

    @DisplayName(" должен запросить комментарий по id")
    @Test
    void shouldExecFindByIdCommand() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> String.format("%s %s", COMMAND_FIND_BY_ID, BOOK_COMMENT_ID))
                .thenReturn(null);

        shell.run(inputProvider);
        verify(resultHandlerService).handle(argumentCaptor.capture());
        verify(bookCommentsService).findById(eq(BOOK_COMMENT_ID));
    }

    @DisplayName(" должен вставить комментарий")
    @Test
    void shouldExecInsertCommand() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> String.format("%s %s %s %s", COMMAND_INSERT, BOOK_COMMENT, AUTHOR_ID, GENRE_ID))
                .thenReturn(null);

        when(bookCommentsService.insert(
                eq(BOOK_COMMENT),
                eq(BOOK_COMMENT_ID))).thenReturn(
                new MongoBookComments(BOOK_COMMENT_ID, BOOK_COMMENT,
                        new MongoBook(BOOK_COMMENT_ID, BOOK_COMMENT,
                                new MongoAuthor(AUTHOR_ID, ""),
                                new MongoGenre(GENRE_ID, ""))));

        shell.run(inputProvider);
        verify(resultHandlerService).handle(argumentCaptor.capture());
        verify(bookCommentsService).insert(eq(BOOK_COMMENT), eq(BOOK_COMMENT_ID));
    }

    @DisplayName(" должен обновить комментарий")
    @Test
    void shouldExecUpdateCommand() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> String.format("%s %s %s %s %s", COMMAND_UPDATE, BOOK_COMMENT_ID, BOOK_COMMENT, AUTHOR_ID, GENRE_ID))
                .thenReturn(null);

        when(bookCommentsService.update(
                eq(BOOK_COMMENT_ID),
                eq(BOOK_COMMENT))).thenReturn(
                new MongoBookComments(BOOK_COMMENT_ID, BOOK_COMMENT, new MongoBook()));

        shell.run(inputProvider);
        verify(resultHandlerService).handle(argumentCaptor.capture());
        verify(bookCommentsService).update(
                eq(BOOK_COMMENT_ID),
                eq(BOOK_COMMENT)
        );
    }

    @DisplayName(" должен удалить комментарий по id")
    @Test
    void shouldExecDeleteCommand() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> String.format("%s %s", COMMAND_DELETE, BOOK_COMMENT_ID))
                .thenReturn(null);

        shell.run(inputProvider);
        verify(resultHandlerService).handle(argumentCaptor.capture());
        verify(bookCommentsService).deleteById(eq(BOOK_COMMENT_ID));
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