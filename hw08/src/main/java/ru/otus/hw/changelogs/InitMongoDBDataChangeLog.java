package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComments;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookCommentsRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    @ChangeSet(order = "000", id = "dropDB", author = "user1", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initFirstBook", author = "user1", runAlways = true)
    public void initFirstBook(BookRepository bookRepository,
                              AuthorRepository authorRepository,
                              GenreRepository genreRepository,
                              BookCommentsRepository bookCommentsRepository) {

        final String id = "1";

        authorRepository.save(new Author(id, "Author_" + id));
        genreRepository.save(new Genre(id,"Genre_" + id));

        bookRepository.save(
          new Book(id, "Book_" + id,
                  authorRepository.findById(id).orElseThrow(),
                  genreRepository.findById(id).orElseThrow()
          )
        );

        bookCommentsRepository.save(new BookComments(id,"Good book",
                bookRepository.findById(id).orElseThrow()));
    }

    @ChangeSet(order = "002", id = "initSecondBook", author = "user1", runAlways = true)
    public void initSecondBook(BookRepository bookRepository,
                              AuthorRepository authorRepository,
                              GenreRepository genreRepository,
                              BookCommentsRepository bookCommentsRepository) {

        final String id = "2";

        authorRepository.save(new Author(id, "Author_" + id));
        genreRepository.save(new Genre(id,"Genre_" + id));

        bookRepository.save(
                new Book(id, "Book_" + id,
                        authorRepository.findById(id).orElseThrow(),
                        genreRepository.findById(id).orElseThrow()
                )
        );
        bookCommentsRepository.save(new BookComments(id,"so so",
                bookRepository.findById(id).orElseThrow()));
    }

    @ChangeSet(order = "003", id = "initThirdBook", author = "user1", runAlways = true)
    public void initThirdBook(BookRepository bookRepository,
                               AuthorRepository authorRepository,
                               GenreRepository genreRepository,
                               BookCommentsRepository bookCommentsRepository) {

        final String id = "3";

        authorRepository.save(new Author(id, "Author_" + id));
        genreRepository.save(new Genre(id,"Genre_" + id));

        bookRepository.save(
                new Book(id, "Book_" + id,
                        authorRepository.findById(id).orElseThrow(),
                        genreRepository.findById(id).orElseThrow()
                )
        );
        bookCommentsRepository.save(new BookComments(id,"already read it",
                bookRepository.findById(id).orElseThrow()));
    }
}
