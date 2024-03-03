package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.repository.QuestionRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CsvQuestionDao implements QuestionDao {

    private final QuestionRepository questionRepository;

    @Override
    public List<QuestionDto> findAll() {
        try {
            return questionRepository.getQuestions();
        } catch (Exception e) {
            throw new QuestionReadException("Error reading Question! ", e);
        }
    }
}
