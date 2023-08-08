package ru.agcon.library_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.agcon.library_app.models.Person;

public interface PeopleRepository extends JpaRepository<Person, Integer> {
}
