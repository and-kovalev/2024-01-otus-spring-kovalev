package ru.otus.hw.repository;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.dto.QuestionDto;

import java.io.BufferedReader;
import java.util.List;

@Component
public class ParserCsvImpl implements Parser {

    @Override
    public List<QuestionDto> parse(BufferedReader bufferedReader) {

        return new CsvToBeanBuilder<QuestionDto>(bufferedReader)
                .withType(QuestionDto.class)
                .withSeparator(';')
                .withSkipLines(1).build().parse();
    }
}
