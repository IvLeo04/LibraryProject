package ru.leonov.LibraryBoot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.leonov.LibraryBoot.model.Book;
import ru.leonov.LibraryBoot.model.Person;
import ru.leonov.LibraryBoot.services.BookService;
import ru.leonov.LibraryBoot.services.PeopleService;

import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {
    private final PeopleService peopleService;
    private final BookService bookService;

    @Autowired
    public BookController(PeopleService peopleService, BookService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }
    //@GetMapping()
    //public String index(Model model) {
    //    model.addAttribute("book", bookService.findAll());
    //    return "/book/index";
    //}
    @GetMapping()
    public String index(Model model,
                        @RequestParam(required = false, defaultValue = "0") int page,
                        @RequestParam(required = false, defaultValue = "0") int size,
                        @RequestParam(required = false, defaultValue = "false") boolean sort_by_year) {
        if (page == 0 && size == 0) {
            if(sort_by_year)
                model.addAttribute("book", bookService.findAll(Sort.by("year")));
            else
                model.addAttribute("book", bookService.findAll());
        }
        else {
            if(sort_by_year)
                model.addAttribute("book", bookService.findAll(PageRequest.of(page, size, Sort.by("year"))).getContent());
            else
                model.addAttribute("book", bookService.findAll(PageRequest.of(page, size)).getContent());
        }
        return "/book/index";
    }
    @GetMapping("/{book_id}")
    public String show(Model model, @PathVariable("book_id") int book_id, @ModelAttribute("person") Person person) {
        Book book1 = bookService.findById(book_id).get();
        model.addAttribute("book", book1);
        model.addAttribute("peopleList", peopleService.findAll());
        model.addAttribute("person", person);
        return "/book/show";
    }
    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "/book/new";
    }
    @PostMapping()
    public String create(@ModelAttribute("book") Book book) {
        bookService.save(book);
        return "redirect:/book";
    }
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "/book/edit";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") Book book, @PathVariable("id") int id) {
        bookService.update(id, book);
        return "redirect:/book";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/book";
    }
    @PatchMapping("/add/{book_id}")
    public String add(@PathVariable("book_id") int book_id, @ModelAttribute("person") Person person) {
        bookService.addPerson(person.getId(), book_id);
        return "redirect:/book";
    }
    @PatchMapping("/{id}/free")
    public String freeBook(@PathVariable("id") int id) {
        bookService.freeBook(id);
        return "redirect:/book";
    }
    @GetMapping("/search")
    public String searchBook(@RequestParam(required = false, defaultValue = "null") String text, Model model) {
        model.addAttribute("text", text);
        return "/book/search";
    }
    @PostMapping("/searchBook")
    public String search(@RequestParam String text, Model model) {
        List<Book> bookList = bookService.findByTitleLike(text);
        model.addAttribute("bookList", bookList);
        return "/book/searchPost";
    }
}
