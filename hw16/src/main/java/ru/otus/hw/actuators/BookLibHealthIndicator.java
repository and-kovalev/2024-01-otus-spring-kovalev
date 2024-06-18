package ru.otus.hw.actuators;

import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

import java.util.Objects;

@Component
@AllArgsConstructor
public class BookLibHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        var books = bookRepository.findAll().collectList().block();

        if (Objects.nonNull(books) && books.size() > 0) {
            return Health.up().withDetail("message", "Есть книги").build();
        } else {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "Библиотека пустая!")
                    .build();
        }
    }
}
