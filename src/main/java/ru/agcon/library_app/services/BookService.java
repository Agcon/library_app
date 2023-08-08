package ru.agcon.library_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.agcon.library_app.models.Book;
import ru.agcon.library_app.models.Person;
import ru.agcon.library_app.repositories.BookRepository;
import ru.agcon.library_app.repositories.PeopleRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PeopleRepository peopleRepository) {
        this.bookRepository = bookRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = bookRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book Book) {
        bookRepository.save(Book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    public List<Book> findAllByOrderByYearOfPublication(){
        return bookRepository.findAllByOrderByYearOfPublication();
    }

    public List<Book> findByNameStartingWith(String starting_with){
        return bookRepository.findByNameStartingWith(starting_with);
    }

    public List<Book> findByName(String name){
        return bookRepository.findByName(name);
    }

    @Transactional
    public void setOwner(int person_id, int id){
        Book updatedBook = findOne(id);
        Person person = peopleRepository.findById(person_id).orElse(null);
        updatedBook.setOwner(person);
        bookRepository.save(updatedBook);
        if (person.getBooks() == null){
            person.setBooks(new ArrayList<>(Collections.singletonList(updatedBook)));
        }
        else {
            person.getBooks().add(updatedBook);
        }
        peopleRepository.save(person);
    }

    @Transactional
    public void removeOwner(int id) {
        Book updatedBook = findOne(id);
        Person person = peopleRepository.findById(updatedBook.getOwner().getId()).orElse(null);
        updatedBook.setOwner(null);
        person.getBooks().remove(updatedBook);
        bookRepository.save(updatedBook);
        peopleRepository.save(person);
    }

}
