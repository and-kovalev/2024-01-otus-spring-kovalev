package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<Genre> findAll() {
        return namedParameterJdbcOperations.query(
                "select id, name from genres",
                new GenreRowMapper()
        );
    }

    @Override
    public Optional<Genre> findById(long id) {
        var genre = namedParameterJdbcOperations.query(
                "select id, name from genres where id = :id",
                Map.of("id", id),
                new GenreRowMapper());

        if (genre.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(genre.get(0));
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            return new Genre(rs.getLong("id"), rs.getString("name"));
        }
    }
}
