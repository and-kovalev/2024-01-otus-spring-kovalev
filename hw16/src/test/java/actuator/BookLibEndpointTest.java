package actuator;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;
import ru.otus.hw.actuators.BookLibHealthIndicator;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(
        classes = BookLibHealthIndicator.class,
        properties = {
                "management.info.env.enabled=true" ,
                "management.endpoints.web.exposure.include=info, health"
        })
@AutoConfigureMockMvc
@EnableAutoConfiguration
public class BookLibEndpointTest {
    private static final String ENDPOINT_PATH = "/actuator/health/bookLib";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void testBookLibUp() throws Exception {
        List<Book> books = List.of(
                new Book("1", "Book_1", new Author(), new Genre()),
                new Book("2", "Book_2", new Author(), new Genre()));
        given(bookRepository.findAll()).willReturn(Flux.just(books.toArray(Book[]::new)));

        mockMvc
                .perform(get(ENDPOINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("UP")));
    }

    @Test
    void testBookLibDown() throws Exception {
        List<Book> books = Collections.emptyList();
        given(bookRepository.findAll()).willReturn(Flux.just(books.toArray(Book[]::new)));

        mockMvc
                .perform(get(ENDPOINT_PATH))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().string(containsString("DOWN")));
    }
}
