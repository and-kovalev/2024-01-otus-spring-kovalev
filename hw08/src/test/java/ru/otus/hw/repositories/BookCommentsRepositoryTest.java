package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.Application;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComments;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с комментариями книги ")
@DataMongoTest
@Import({Application.class})
class BookCommentsRepositoryTest {

    private static final String TEST_ID = "1";
    private static final String TEST_TWO_ID = "2";
    private static final String BOOK_COMMENTS_OK = "BookComments_OK";

    @Autowired
    private BookCommentsRepository bookCommentsRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static List<String> getCommentsId() {
        return IntStream.range(1, 4).boxed()
                .map(String::valueOf)
                .toList();
    }

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(BookComments.class);
        IntStream.range(1, 4).boxed()
                .forEach(index -> bookCommentsRepository.save(
                        new BookComments(String.valueOf(index),
                                "Comment_" + String.valueOf(index),
                                null/*new Book(String.valueOf(index),
                                        "Book_" + String.valueOf(index),
                                        new Author(String.valueOf(index), "Author_1" + String.valueOf(index)),
                                        new Genre(String.valueOf(index), "Genre_1" + String.valueOf(index))
                                )*/
                        )
                ));
    }

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getCommentsId")
    void shouldReturnCorrectBookCommentsById(String testId) {
        var actualBookComments = bookCommentsRepository.findById(testId);
        var expectedBookComments = mongoTemplate.findById(testId, BookComments.class);
        assertThat(actualBookComments).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBookComments);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewBookComments() {
        var expectedBookComments = new BookComments(null, BOOK_COMMENTS_OK,
                null);
        var returnedBookComments = bookCommentsRepository.save(expectedBookComments);
        assertThat(returnedBookComments).isNotNull()
                .matches(book -> Objects.nonNull(book.getId()))
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBookComments);

        assertThat(bookCommentsRepository.findById(returnedBookComments.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(returnedBookComments);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedBookComments() {
        var expectedBookComments = new BookComments(TEST_ID, BOOK_COMMENTS_OK,
                null);

        assertThat(bookCommentsRepository.findById(expectedBookComments.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isNotEqualTo(expectedBookComments);

        var returnedBook = bookCommentsRepository.save(expectedBookComments);
        assertThat(returnedBook).isNotNull()
                .matches(book -> Objects.nonNull(book.getId()))
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBookComments);

        assertThat(bookCommentsRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteBookComments() {
        assertThat(bookCommentsRepository.findById(TEST_ID)).isPresent();
        bookCommentsRepository.deleteById(TEST_ID);
        assertThat(bookCommentsRepository.findById(TEST_ID)).isEmpty();
    }
}