package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Методы сервиса тестирования студента")
@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @InjectMocks
    private TestServiceImpl testService;

    @Mock
    private LocalizedIOService ioService;

    @Mock
    private CsvQuestionDao questionDao;

    private final String FIRST_NAME = "Ivan";
    private final String SECOND_NAME = "Ivanov";
    private final Student student = new Student(FIRST_NAME,SECOND_NAME);

    @Test
    @DisplayName("Вызывать методы ioService и questionDao с нужными параметрами. Текущий метод: executeTestFor")
    void executeTest() {
        testService.executeTestFor(student);

        verify(ioService).printLine(any());
        verify(ioService).printLineLocalized(any());
        verify(questionDao).findAll();
    }

    @Test
    @DisplayName("Бросать исключение при ошибке чтения вопросов")
    void executeThrowQuestionReadException() {
        when(questionDao.findAll()).thenThrow(new QuestionReadException("Error reading Question! ", new Exception()));
        assertThatThrownBy(() -> testService.executeTestFor(student)).isInstanceOf(QuestionReadException.class);
    }
}