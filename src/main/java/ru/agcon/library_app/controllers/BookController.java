package ru.agcon.library_app.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.agcon.library_app.models.Book;
import ru.agcon.library_app.services.BookService;
import ru.agcon.library_app.services.PeopleService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "sort_by_year", required = false) boolean sort_by_year,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer books_per_page) {
        List<Book> books;
        if (sort_by_year){
            books = bookService.findAllByOrderByYearOfPublication();
        }
        else {
            books = bookService.findAll();
        }
        if (page != null){
            List<Book> books_for_show = new ArrayList<>();
            for (int i = (page - 1) * books_per_page; i < page * books_per_page; i++){
                if (i == books.size()) break;
                books_for_show.add(books.get(i));
            }
            books = books_for_show;
        }
        model.addAttribute("books", books);
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findOne(id));
        model.addAttribute("people", peopleService.findAll());
        return "books/show";
    }

    @GetMapping("/search")
    public String searchPage(){
        return "books/search";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute("query") String query, Model model){
        List<Book> books = bookService.findByNameStartingWith(query);
        model.addAttribute("books", books);
        return "books/search";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findOne(id));
        return "books/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        bookService.update(id, book);
        return "redirect:/books";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PostMapping("/setowner/{id}")
    public String setOwner(@PathVariable("id") int id, @RequestParam(name = "person_id", required = false) Integer person_id){
        bookService.setOwner(person_id, id);
        return "redirect:/books/{id}";
    }

    @PostMapping("/removeowner/{id}")
    public String removeOwner(@PathVariable("id") int id){
        bookService.removeOwner(id);
        return "redirect:/books/{id}";
    }
}
