package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.Application;
import ru.otus.hw.models.BookComments;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями книги ")
@DataJpaTest
@Import({Application.class})
class JpaBookCommentsRepositoryTest {

    private static final long TEST_ID = 1L;
    private static final long TEST_TWO_ID = 2L;
    private static final String BOOK_COMMENTS_OK = "BookComments_OK";

    @Autowired
    private BookCommentsRepository jpaBookCommentsRepository;

    @Autowired
    private BookRepository jpaBookRepository;

    @Autowired
    private TestEntityManager em;

    private static List<Long> getCommentsId() {
        return IntStream.range(1, 4).boxed()
                .map(Long::valueOf)
                .toList();
    }

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getCommentsId")
    void shouldReturnCorrectBookCommentsById(long testId) {
        var actualBookComments = jpaBookCommentsRepository.findById(testId);
        var expectedBookComments = em.find(BookComments.class, testId);
        assertThat(actualBookComments).isPresent()
                .get()
                .isEqualTo(expectedBookComments);
    }

    @DisplayName("должен загружать все комментарии книги")
    @Test
    void shouldReturnAllBookComments() {
        var book = jpaBookRepository.findById(TEST_ID);
        assertThat(book.get().getBookComments()).size().isPositive();
        var actualBookComments = book.get().getBookComments();
        assertThat(actualBookComments).isNotEmpty()
                .containsExactlyElementsOf(List.of(jpaBookCommentsRepository.findById(TEST_ID).get()));
    }

    @DisplayName("должен загружать только комментарии одной книги")
    @Test
    void shouldReturnCorrectBookCommentsList() {
        var book = jpaBookRepository.findById(TEST_TWO_ID);
        assertThat(book.get().getBookComments()).size().isPositive();
        var actualBookComments = book.get().getBookComments();
        var expectedBookComments = List.of(jpaBookCommentsRepository.findById(TEST_TWO_ID).get(),
                jpaBookCommentsRepository.findById(TEST_TWO_ID + TEST_ID).get());

        assertThat(actualBookComments).containsExactlyElementsOf(expectedBookComments);
        actualBookComments.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewBookComments() {
        var expectedBookComments = new BookComments(0, BOOK_COMMENTS_OK,
                jpaBookRepository.findById(TEST_ID).get());
        var returnedBookComments = jpaBookCommentsRepository.save(expectedBookComments);
        assertThat(returnedBookComments).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBookComments);

        assertThat(jpaBookCommentsRepository.findById(returnedBookComments.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBookComments);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedBookComments() {
        var expectedBookComments = new BookComments(TEST_ID, BOOK_COMMENTS_OK,
                jpaBookRepository.findById(TEST_ID).get());

        assertThat(jpaBookCommentsRepository.findById(expectedBookComments.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBookComments);

        var returnedBook = jpaBookCommentsRepository.save(expectedBookComments);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBookComments);

        assertThat(jpaBookCommentsRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteBookComments() {
        assertThat(jpaBookCommentsRepository.findById(TEST_ID)).isPresent();
        jpaBookCommentsRepository.deleteById(TEST_ID);
        assertThat(jpaBookCommentsRepository.findById(TEST_ID)).isEmpty();
    }
}