package ru.leonov.LibraryBoot.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    @NotNull(message = "Данное поле не может быть пустым")
    @Size(min = 2, max = 50, message = "Данное поле может содержать от 2 до 50 символов")
    private String title;

    @Column(name = "author")
    @NotNull(message = "Данное поле не может быть пустым")
    @Size(min = 2, max = 50, message = "Данное поле может содержать от 2 до 30 символов")
    private String author;

    @Column(name = "year")
    @Min(value = 1500, message = "Год издания книги должен быть > 1500")
    @Max(value = 2023, message = "Год издания книги должен быть < 2023")
    private int year;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "book_term")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeBook;

    @Transient
    private Boolean isTerm;


    public Book() {}
    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getTimeBook() {
        return timeBook;
    }

    public void setTimeBook(Date timeBook) {
        this.timeBook = timeBook;
    }

    public Boolean getTerm() {
        return isTerm;
    }

    public void setTerm(Boolean term) {
        isTerm = term;
    }
}
