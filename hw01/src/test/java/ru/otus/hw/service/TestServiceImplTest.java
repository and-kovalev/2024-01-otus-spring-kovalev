package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Методы сервиса чтения вопросов")
@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    private TestService testService;

    @Mock
    private IOService ioService;

    @Mock
    private CsvQuestionDao questionDao;

    @BeforeEach
    void setUp() {
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    @DisplayName("Вызывать методы ioService и questionDao с нужными параметрами. Текущий метод: executeTest")
    void executeTest() {
        testService.executeTest();

        verify(ioService).printLine(any());
        verify(ioService).printFormattedLine(any());
        verify(questionDao).findAll();
    }

    @Test
    @DisplayName("Бросать исключение при ошибке чтения вопросов")
    void executeThrowQuestionReadException() {
        when(questionDao.findAll()).thenThrow(new QuestionReadException("Error reading Question! ", new Exception()));
        assertThatThrownBy(() -> testService.executeTest()).isInstanceOf(QuestionReadException.class);

    }
}