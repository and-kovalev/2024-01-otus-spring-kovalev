package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        questions.forEach(question -> testResult.applyAnswer(question.toDomainObject(), getAnswer(question)));

        return testResult;
    }

    private boolean getAnswer(QuestionDto question) {
        int rightAnswer = 0;
        ioService.printLine("Question:");
        ioService.printLine(question.getText());
        ioService.printLine("");
        ioService.printLine("Answers:");
        for (int i = 0; i < question.getAnswers().size();i++) {
            ioService.printFormattedLine("%s: %s",i + 1, question.getAnswers().get(i).text());

            if (question.getAnswers().get(i).isCorrect()) {
                rightAnswer = i + 1;
            }
        }
        ioService.printLine("");
        var studentAnswer = ioService.readStringWithPrompt("Please input number your answer");

        return String.valueOf(rightAnswer).equals(studentAnswer);
    }
}
