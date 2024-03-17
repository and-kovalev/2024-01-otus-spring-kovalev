package ru.otus.hw.repository;

import ru.otus.hw.dao.dto.QuestionDto;

import java.util.List;

public interface QuestionRepository {
    List<QuestionDto> getQuestions();
}
