package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.BookComments;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBookCommentsRepository implements BookCommentsRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<BookComments> findAllForBook(long bookId) {
        TypedQuery<BookComments> query = em.createQuery("select bc from BookComments bc " +
                " join fetch book b " +
                "where b.id = :book_id ", BookComments.class);
        query.setParameter("book_id", bookId);
        return query.getResultList();
    }

    @Override
    public Optional<BookComments> findById(long id) {
        return Optional.ofNullable(em.find(BookComments.class, id));
    }

    @Override
    public BookComments save(BookComments bookComment) {
        if (bookComment.getId() == 0) {
            em.persist(bookComment);
            return bookComment;
        }
        return em.merge(bookComment);
    }

    @Override
    public void deleteById(long id) {
        findById(id).ifPresent(em::remove);
    }
}
