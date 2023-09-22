package ru.leonov.LibraryBoot.services;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leonov.LibraryBoot.model.Book;
import ru.leonov.LibraryBoot.model.Person;
import ru.leonov.LibraryBoot.repositories.BookRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final EntityManager entityManager;

    @Autowired
    public BookService(BookRepository bookRepository, EntityManager entityManager) {
        this.bookRepository = bookRepository;
        this.entityManager = entityManager;
    }
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }
    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }
    @Transactional
    public void update(int id, Book book) {
        Book toBookUpdated = bookRepository.findById(id).get();
        book.setId(id);
        book.setOwner(toBookUpdated.getOwner());
        bookRepository.save(book);
    }
    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }
    @Transactional
    public void addPerson(int person_id, int book_id) {
        Session session = entityManager.unwrap(Session.class);
        Book book = session.get(Book.class, book_id);
        Person person = session.get(Person.class, person_id);
        book.setOwner(person);
        Date date = new Date();
        book.setTimeBook(date);
        System.out.println(date);
        if(person.getBooks().isEmpty())
            person.setBooks(new ArrayList<>(Collections.singletonList(book)));
        else
            person.getBooks().add(book);
    }
    @Transactional
    public void freeBook(int id) {
        Session session = entityManager.unwrap(Session.class);
        Book book = session.get(Book.class, id);
        book.setTerm(false);
        book.setTimeBook(null);
        if(book.getOwner().getBooks().size() == 1)
            book.getOwner().setBooks(null);
        else
            book.getOwner().getBooks().remove(findById(id).get());
        book.setOwner(null);
    }
    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }
    public List<Book> findAll(Sort var1) {
        return bookRepository.findAll(var1);
    }
    public List<Book> findByTitleLike(String nameLike) {
        return bookRepository.findByTitleLike(nameLike + "%");
    }
}
