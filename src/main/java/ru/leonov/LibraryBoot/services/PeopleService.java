package ru.leonov.LibraryBoot.services;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leonov.LibraryBoot.model.Book;
import ru.leonov.LibraryBoot.model.Person;
import ru.leonov.LibraryBoot.repositories.PeopleRepository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    private final EntityManager entityManager;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, EntityManager entityManager) {
        this.peopleRepository = peopleRepository;
        this.entityManager = entityManager;
    }
    public List<Person> findAll() {
        return peopleRepository.findAll();
    }
    public Person findById(int id) {
        Person person = peopleRepository.findById(id).get();
        Date date = new Date();
        for (Book book:person.getBooks()) {
            long i = date.getTime() - book.getTimeBook().getTime();
            long j = TimeUnit.DAYS.convert(i, TimeUnit.MILLISECONDS);
            System.out.println(j);
            boolean bool = j >= 10;
            book.setTerm(bool);
        }
        return peopleRepository.findById(id).get();
    }
    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }
    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }
    @Transactional
    public void delete(int id) {
        Session session = entityManager.unwrap(Session.class);
        Person person = session.get(Person.class, id);
        for (Book book:person.getBooks()) {
            book.setTimeBook(null);
            book.setTerm(false);
        }
        peopleRepository.deleteById(id);
    }
    public List<Person> findByFullName(String name) {
        return peopleRepository.findByName(name);
    }
}
