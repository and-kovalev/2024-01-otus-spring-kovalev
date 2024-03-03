package ru.otus.hw.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("Методы сервиса чтения вопросов")
@ExtendWith(MockitoExtension.class)
class QuestionRepositoryImplTest {
    private static final int NUMBER_QUESTION = 3;
    private static final String TEST_FILE_NAME = "questions_test.csv";
    private static final String BAD_TEST_FILE_NAME = "qqq_test.csv";

    @Mock
    private TestFileNameProvider fileNameProvider;

    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        when(fileNameProvider.getTestFileName()).thenReturn(TEST_FILE_NAME);
        questionRepository = new QuestionRepositoryImpl(fileNameProvider);
    }

    @Test
    @DisplayName("Корректный вызов чтения вопросов, метод:getQuestions")
    void getQuestionFromCsvSuccess() {

        var questions = questionRepository.getQuestions();

        assertThat(questions).isNotEmpty();
        assertEquals(NUMBER_QUESTION, questions.size());
    }

    @Test
    @DisplayName("Ошибка чтения при некорректном имени файла вопросов, метод:getQuestions")
    void getQuestionFromCsvException() {
        when(fileNameProvider.getTestFileName()).thenReturn(BAD_TEST_FILE_NAME);

        assertThatThrownBy(() -> questionRepository.getQuestions()).isInstanceOf(NullPointerException.class);
    }

}