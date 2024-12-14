package com.leherryacademy.LeHerryAcademy.service;

import com.leherryacademy.LeHerryAcademy.model.Person;
import com.leherryacademy.LeHerryAcademy.reponsitory.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public Page<Person> getPersonsPage(int page, int size, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return personRepository.findAll(pageable);
    }
}
