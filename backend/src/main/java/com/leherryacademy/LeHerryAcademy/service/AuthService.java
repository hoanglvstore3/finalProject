package com.leherryacademy.LeHerryAcademy.service;

import com.leherryacademy.LeHerryAcademy.model.Person;
import com.leherryacademy.LeHerryAcademy.model.Roles;
import com.leherryacademy.LeHerryAcademy.reponsitory.PersonRepository;
import com.leherryacademy.LeHerryAcademy.reponsitory.RolesRepository;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    RolesRepository rolesRepository;
    public Person createPerson(Person person){
        return personRepository.readByEmailAndAccountFrom(person.getEmail(), person.getAccountFrom())
                .orElseGet(() -> {
                    String password = "$2a$12$/YKuOlBMuIdzL.2AldN2M.KVtkOixCDH4eRXnSjElKUikDNiNtmmq";
                    Roles roles = rolesRepository.getByRoleName("STUDENT");
                    person.setRoles(roles);
                    person.setPwd(password);
                    return personRepository.save(person);
                });
    }

}
