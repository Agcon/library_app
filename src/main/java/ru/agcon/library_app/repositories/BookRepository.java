package ru.agcon.library_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.agcon.library_app.models.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByName(String name);
    List<Book> findAllByOrderByYearOfPublication();
    List<Book> findByNameStartingWith(String starting_with);
}
