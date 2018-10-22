package com.springcontext.springexceptionhandling.services;

import com.springcontext.springexceptionhandling.entities.Address;
import com.springcontext.springexceptionhandling.entities.Person;
import com.springcontext.springexceptionhandling.exceptions.ConflictException;
import com.springcontext.springexceptionhandling.exceptions.NotFoundException;
import com.springcontext.springexceptionhandling.repositories.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person create(Person person) {
        log.info("Adding a new person - firstname {}, lastname {}", person.getFirstname(), person.getLastname());

        return this.personRepository.save(person);
    }

    public Person getById(long id) throws NotFoundException {
        log.info("Trying to fetch Person with id {}", id);

        Optional<Person> person = this.personRepository.findById(id);

        if (person.isPresent()) {
            return person.get();
        }

        log.error("No person found with id {}", id);
        throw new NotFoundException("This person does not exist");
    }

    public Person addAddress(long id, Address address) throws ConflictException {

        Person person;

        try {
            person = this.getById(id);
        } catch (NotFoundException e) {
            throw new ConflictException(e.getMessage());
        }

        log.info("Adding new address to Person with id {}", id);

        person.addAddress(address);

        return this.personRepository.save(person);
    }

    public void delete(long id) {
        log.info("Deleting Person with id {}", id);

        this.personRepository.deleteById(id);
    }

    public void removeCompany(Person person) {
        person.setCompany(null);

        this.personRepository.save(person);
    }
}