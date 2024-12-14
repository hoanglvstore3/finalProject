package com.leherryacademy.LeHerryAcademy.reponsitory;

import com.leherryacademy.LeHerryAcademy.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person readByEmail(String email);

    Page<Person> findAll(Pageable pageable);

    Optional<Person>  readByEmailAndAccountFrom(String email, String accountFrom);


}
