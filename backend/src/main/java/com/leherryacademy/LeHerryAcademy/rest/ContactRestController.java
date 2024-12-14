package com.leherryacademy.LeHerryAcademy.rest;

import com.leherryacademy.LeHerryAcademy.model.Person;
import com.leherryacademy.LeHerryAcademy.model.Reponse;
import com.leherryacademy.LeHerryAcademy.model.Roles;
import com.leherryacademy.LeHerryAcademy.reponse.PersonsReponse;
import com.leherryacademy.LeHerryAcademy.reponsitory.PersonRepository;
import com.leherryacademy.LeHerryAcademy.reponsitory.RolesRepository;
import com.leherryacademy.LeHerryAcademy.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/api/contact",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
public class ContactRestController {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PersonService personService;

    @GetMapping("/persons")
    //@ResponseBody
    public List<Person> getPersons(){
        return personRepository.findAll();
    }

    @GetMapping("/person")
    public Person getPerson(@RequestParam String email) {
        return personRepository.readByEmail(email);
    }
    @PostMapping("/createPerson")
    public ResponseEntity<Reponse> createPerson(@RequestBody Person person){
        Roles role = rolesRepository.getByRoleName("STUDENT");
        person.setRoles(role);
        person.setPwd(passwordEncoder.encode(person.getPwd()));
        personRepository.save(person);
        Reponse response = new Reponse("200", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    @PutMapping("/updatePerson/{personId}")
    public ResponseEntity<Reponse> updatePerson(@PathVariable int personId, @RequestBody Person person){
        Optional<Person> personUpdateOptinal = personRepository.findById(personId);
        if(personUpdateOptinal.isPresent()){
            Person personUpdate = personUpdateOptinal.get();
            personUpdate.setName(person.getName());
            personUpdate.setMobileNumber(person.getMobileNumber());
            personUpdate.setEmail(person.getEmail());
            Roles role = rolesRepository.getByRoleName("STUDENT");
            personUpdate.setRoles(role);
            personRepository.save(personUpdate);
            Reponse response = new Reponse("200", true);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }
        else {
            Reponse response = new Reponse("404", false);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(response);
        }
    }
    @DeleteMapping("/deletePerson/{personId}")
    public ResponseEntity<Reponse> deletePerson(@PathVariable int personId){
        personRepository.deleteById(personId);
        Reponse response = new Reponse("200", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }
    @GetMapping("/getPersonByID/{personId}")
    public Person getPersonByID(@PathVariable int personId){
        Optional<Person> personGet = personRepository.findById(personId);
        Person person =  personGet.get();
        return person;
    }

    @GetMapping("/personspage")
    public PersonsReponse getPersons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Page<Person> personPage = personService.getPersonsPage(page, size, sortField, sortDirection);

        return new PersonsReponse(personPage.getTotalPages(),personPage.getContent());
    }




}
