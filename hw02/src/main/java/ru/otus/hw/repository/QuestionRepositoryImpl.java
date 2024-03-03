package ru.otus.hw.repository;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<QuestionDto> getQuestions() {
        return new CsvToBeanBuilder(
                new BufferedReader(
                        new InputStreamReader(getClass()
                                .getClassLoader()
                                .getResourceAsStream(fileNameProvider.getTestFileName()))))
                .withType(QuestionDto.class)
                .withSeparator(';')
                .withSkipLines(1).build().parse();
    }

}
