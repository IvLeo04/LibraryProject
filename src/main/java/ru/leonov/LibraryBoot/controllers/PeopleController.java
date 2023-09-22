package ru.leonov.LibraryBoot.controllers;

import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.leonov.LibraryBoot.model.Book;
import ru.leonov.LibraryBoot.model.Person;
import ru.leonov.LibraryBoot.services.PeopleService;
import ru.leonov.LibraryBoot.util.PersonValidator;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final PersonValidator personValidator;
    private final EntityManager entityManager;

    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator, EntityManager entityManager) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
        this.entityManager = entityManager;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "/people/index";
    }
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public String show(Model model, @PathVariable("id") int id) {
        Person person = peopleService.findById(id);
        System.out.println(person.getBooks());
        model.addAttribute("person", person);
        List<Book> list = person.getBooks();
        model.addAttribute("listBook", list);
        return "/people/show";
    }

    @GetMapping("/new")
    public String newPerson(Model model) {
        model.addAttribute("person", new Person());
        return "/people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors())
            return "/people/new";
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findById(id));
        return "/people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors())
            return "/people/edit";
        peopleService.update(id, person);
        return "redirect:/people";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
