package com.leherryacademy.LeHerryAcademy.rest;

import com.leherryacademy.LeHerryAcademy.model.Person;
import com.leherryacademy.LeHerryAcademy.reponsitory.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/api/person",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
public class PersonRestController {
    @Autowired
    PersonRepository personRepository;
    @GetMapping("/auth/getPerson")
    public Person getPerson(@RequestParam String email, @RequestParam String accountFrom) {
        Optional<Person> user = personRepository.readByEmailAndAccountFrom(email,accountFrom);
        if (user.isPresent()){
            return user.get();
        }
        return null;

    }
}
