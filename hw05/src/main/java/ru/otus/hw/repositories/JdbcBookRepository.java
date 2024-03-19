package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Optional<Book> findById(long id) {
        var book = namedParameterJdbcOperations.query(
                "select b.id, b.title, b.author_id, b.genre_id, a.full_name, g.name " +
                        "from books b " +
                        "inner join authors a on a.id = b.author_id " +
                        "inner join genres g on g.id = b.genre_id " +
                        "where b.id = :id ",
                Map.of("id", id),
                new BookRowMapper());

        if (book.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(book.get(0));
    }

    @Override
    public List<Book> findAll() {
        return namedParameterJdbcOperations.query(
                "select b.id, b.title, b.author_id, b.genre_id, a.full_name, g.name " +
                        "from books b " +
                        "inner join authors a on a.id = b.author_id " +
                        "inner join genres g on g.id = b.genre_id ",
                new BookRowMapper()
        );
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from books where id = :id",
                Map.of("id", id)
        );
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("author_id", book.getAuthor().getId())
                .addValue("genre_id", book.getGenre().getId());

        namedParameterJdbcOperations.update(
                "insert into books (title, author_id, genre_id) " +
                        "values (:title, :author_id, :genre_id) ",
                params, keyHolder, new String[]{"id"}
        );

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", book.getId())
                .addValue("title", book.getTitle())
                .addValue("author_id", book.getAuthor().getId())
                .addValue("genre_id", book.getGenre().getId());

        var rowUpdated = namedParameterJdbcOperations.update(
                "update books " +
                        "set title = :title, " +
                        "author_id = :author_id, " +
                        "genre_id = :genre_id " +
                        "where id = :id ",
                params
        );

        if (rowUpdated == 0) {
            throw new EntityNotFoundException("No rows was updated!");
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(rs.getLong("id"), rs.getString("title"),
                    new Author(rs.getLong("author_id"), rs.getString("full_name")),
                    new Genre(rs.getLong("genre_id"), rs.getString("name"))
            );
        }
    }
}
