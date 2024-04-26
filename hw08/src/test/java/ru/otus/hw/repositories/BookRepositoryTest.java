package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.shell.InputProvider;
import ru.otus.hw.Application;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("Репозиторий на основе MongoDB для работы с книгами ")
@DataMongoTest
@Import({Application.class})
class BookRepositoryTest {

    private static final String TEST_ID = "1";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
         mongoTemplate.dropCollection(Book.class);
         IntStream.range(1, 4).boxed()
                .forEach(index -> mongoTemplate.save(
                         new Book(String.valueOf(index),
                                 "Book_" + String.valueOf(index),
                                 new Author(String.valueOf(index), "Author_1" + String.valueOf(index)),
                                 new Genre(String.valueOf(index), "Genre_1" + String.valueOf(index))
                         )
                 ));
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getBookIds")
    void shouldReturnCorrectBookById(String testBook) {
        var actualBook = bookRepository.findById(testBook);
        var expectedBook = mongoTemplate.findById(testBook, Book.class);
        assertThat(actualBook).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать все книги")
    @Test
    void shouldReturnAllBooks() {
        var actualBooks = bookRepository.findAll();
        assertThat(actualBooks).isNotEmpty()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(getDbBooks());
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepository.findAll();
        var expectedBooks = getDbBooks();

        assertThat(actualBooks).isNotNull()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBooks);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(null, "BookTitle_10500",
                authorRepository.findById(TEST_ID).get(),
                genreRepository.findById(TEST_ID).get());
        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> Objects.nonNull(book.getId()))
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(TEST_ID, "BookTitle_10500",
                authorRepository.findById(TEST_ID).get(),
                genreRepository.findById(TEST_ID).get());

        assertThat(bookRepository.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> Objects.nonNull(book.getId()))
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(bookRepository.findById(TEST_ID)).isPresent();
        bookRepository.deleteById(TEST_ID);
        assertThat(bookRepository.findById(TEST_ID)).isEmpty();
    }

    private List<Book> getDbBooks() {
        return IntStream.range(1, 4).boxed()
                .map(id -> mongoTemplate.findById(String.valueOf(id), Book.class))
                .toList();
    }

    private static List<String> getBookIds() {
        return IntStream.range(1, 4).boxed()
                .map(String::valueOf)
                .toList();
    }
}