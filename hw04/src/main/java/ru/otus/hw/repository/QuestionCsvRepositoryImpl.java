package ru.otus.hw.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionCsvRepositoryImpl implements QuestionRepository {
    private final TestFileNameProvider fileNameProvider;

    private final Parser parser;

    private final Reader reader;

    @Override
    public List<QuestionDto> getQuestions() {
        return parser.parse(
                reader.getData(fileNameProvider.getTestFileName()));
    }

}
