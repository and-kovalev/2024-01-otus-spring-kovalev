package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тест команд IOService ")
@SpringBootTest
class LocalizedIOServiceImplTest {

    private final static String IN_TEXT = "input";
    private final static int IN_NUMBER = 3;
    private final static int MIN_INPUT_ANSWER = 1;
    private final static int MAX_INPUT_ANSWER = 5;
    private final static String PROMT = "ask";
    private final static String ERROR = "error";
    private final static String CODE = "code";
    private final static String ARGS = "arg1";

    @Autowired
    private LocalizedIOService localizedIOService;

    @MockBean(name = "localizedMessagesServiceImpl")
    private LocalizedMessagesService localizedMessagesService;

    @MockBean(name = "streamsIOService")
    private IOService ioService;

    @Test
    void printLine() {
        localizedIOService.printLine("");
        verify(ioService).printLine(anyString());
    }

    @Test
    void printFormattedLine() {
        localizedIOService.printFormattedLine("",ARGS);
        verify(ioService).printFormattedLine(anyString(), any());
    }

    @Test
    void readString() {
        when(ioService.readString()).thenReturn(IN_TEXT);
        assertEquals(IN_TEXT,localizedIOService.readString());
        verify(ioService).readString();
    }

    @Test
    void readStringWithPrompt() {
        when(ioService.readStringWithPrompt(PROMT)).thenReturn(IN_TEXT);
        assertEquals(IN_TEXT,localizedIOService.readStringWithPrompt(PROMT));
        verify(ioService).readStringWithPrompt(PROMT);
    }

    @Test
    void readIntForRange() {
        when(ioService.readIntForRange(MIN_INPUT_ANSWER,MAX_INPUT_ANSWER, ERROR)).thenReturn(IN_NUMBER);
        assertEquals(IN_NUMBER,localizedIOService.readIntForRange(MIN_INPUT_ANSWER,MAX_INPUT_ANSWER, ERROR));
        verify(ioService).readIntForRange(MIN_INPUT_ANSWER,MAX_INPUT_ANSWER, ERROR);
    }

    @Test
    void readIntForRangeWithPrompt() {
        when(ioService.readIntForRangeWithPrompt(MIN_INPUT_ANSWER,MAX_INPUT_ANSWER, PROMT, ERROR)).thenReturn(IN_NUMBER);
        assertEquals(IN_NUMBER,localizedIOService.readIntForRangeWithPrompt(MIN_INPUT_ANSWER,MAX_INPUT_ANSWER, PROMT, ERROR));
        verify(ioService).readIntForRangeWithPrompt(MIN_INPUT_ANSWER,MAX_INPUT_ANSWER, PROMT, ERROR);
    }

    @Test
    void printLineLocalized() {
        when(localizedMessagesService.getMessage(CODE)).thenReturn(IN_TEXT);
        localizedIOService.printLineLocalized(CODE);
        verify(ioService).printLine(IN_TEXT);
    }

    @Test
    void printFormattedLineLocalized() {
        when(localizedMessagesService.getMessage(CODE, ARGS)).thenReturn(IN_TEXT);
        localizedIOService.printFormattedLineLocalized(CODE, ARGS);
        verify(ioService).printLine(IN_TEXT);
    }

    @Test
    void readStringWithPromptLocalized() {
        when(localizedMessagesService.getMessage(CODE)).thenReturn(IN_TEXT);
        when(ioService.readStringWithPrompt(PROMT)).thenReturn(IN_TEXT);
        localizedIOService.readStringWithPromptLocalized(CODE);
        verify(ioService).readStringWithPrompt(IN_TEXT);
    }

    @Test
    void readIntForRangeLocalized() {
        when(localizedMessagesService.getMessage(CODE)).thenReturn(ERROR);
        when(ioService.readIntForRange(MIN_INPUT_ANSWER,MAX_INPUT_ANSWER, ERROR)).thenReturn(IN_NUMBER);
        assertEquals(IN_NUMBER,localizedIOService.readIntForRange(MIN_INPUT_ANSWER,MAX_INPUT_ANSWER, ERROR));
        verify(ioService).readIntForRange(MIN_INPUT_ANSWER,MAX_INPUT_ANSWER, ERROR);
    }

    @Test
    void readIntForRangeWithPromptLocalized() {
        when(localizedMessagesService.getMessage(CODE)).thenReturn(ERROR);
        when(localizedMessagesService.getMessage(PROMT)).thenReturn(PROMT);
        when(ioService.readIntForRangeWithPrompt(MIN_INPUT_ANSWER,MAX_INPUT_ANSWER, PROMT, ERROR)).thenReturn(IN_NUMBER);
        assertEquals(IN_NUMBER,localizedIOService.readIntForRangeWithPromptLocalized(MIN_INPUT_ANSWER,MAX_INPUT_ANSWER, PROMT, CODE));
        verify(ioService).readIntForRangeWithPrompt(MIN_INPUT_ANSWER,MAX_INPUT_ANSWER, PROMT, ERROR);
    }

    @Test
    void getMessage() {
        localizedIOService.getMessage(CODE, ARGS);
        verify(localizedMessagesService).getMessage(CODE, ARGS);
    }
}