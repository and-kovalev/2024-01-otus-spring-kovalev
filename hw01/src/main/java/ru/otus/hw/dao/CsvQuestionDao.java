package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<QuestionDto> findAll() {
        try {
            return new CsvToBeanBuilder(
                    new BufferedReader(
                            new InputStreamReader(getClass()
                                    .getClassLoader()
                                    .getResourceAsStream(fileNameProvider.getTestFileName()))))
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1).build().parse();
        } catch (Exception e) {
            throw new QuestionReadException("Error reading Question! ", e);
        }
    }
}
