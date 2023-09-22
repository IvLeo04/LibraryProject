package ru.leonov.LibraryBoot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "Person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotNull(message = "Данное поле не может быть пустым")
    @Size(min = 2, max = 40, message = "Данное поле может содержать от 2 до 40 символов")
    private String name;

    @Column(name = "year_of_birth")
    @Min(value = 1900, message = "Год рождения не может быть меньше 1900")
    @Max(value = 2023, message = "Год рождения не может быть больше 2023")
    private int year_birth;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person() {}

    public Person(int id, String name, int year_birth) {
        this.name = name;
        this.id = id;
        this.year_birth = year_birth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String full_name) {
        this.name = full_name;
    }

    public int getYear_birth() {
        return year_birth;
    }

    public void setYear_birth(int year_birth) {
        this.year_birth = year_birth;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
