package com.springcontext.springexceptionhandling.controllers;

import com.springcontext.springexceptionhandling.controllers.dto.address.AddressDto;
import com.springcontext.springexceptionhandling.controllers.dto.person.PersonRequestDto;
import com.springcontext.springexceptionhandling.controllers.dto.person.PersonResponseDto;
import com.springcontext.springexceptionhandling.entities.Address;
import com.springcontext.springexceptionhandling.entities.Company;
import com.springcontext.springexceptionhandling.entities.Person;
import com.springcontext.springexceptionhandling.exceptions.ConflictException;
import com.springcontext.springexceptionhandling.exceptions.NotFoundException;
import com.springcontext.springexceptionhandling.services.CompanyService;
import com.springcontext.springexceptionhandling.services.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class PersonController {

    private static final String API_CALL_MESSAGE = "New API call: ";
    private static final String FAILURE_MESSAGE = "Failure: ";
    private static final String SUCCESS_MESSAGE = "Success: ";

    private final PersonService personService;

    private final CompanyService companyService;

    @Autowired
    public PersonController(PersonService personService, CompanyService companyService) {
        this.personService = personService;
        this.companyService = companyService;
    }

    @PostMapping(path = "/person", consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonResponseDto create(@RequestBody PersonRequestDto person) throws NotFoundException {

        log.info(API_CALL_MESSAGE + "Create a person");

        if (Objects.isNull(person)) {
            log.error(FAILURE_MESSAGE + "The request body is null");
            return null;
        }

        Person createdPerson = this.personService.create(this.convertRequestDto(person));

        log.info(SUCCESS_MESSAGE + "New person created!");

        return PersonController.convertModel(createdPerson);
    }

    @GetMapping(path = "/person/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonResponseDto getPerson(@PathVariable long id) throws NotFoundException {

        log.info(API_CALL_MESSAGE + "Get a person");

        Person person = this.personService.getById(id);

        log.info(SUCCESS_MESSAGE + "Returning person with id {}", id);
        return PersonController.convertModel(person);
    }

    @PutMapping(
            path = "/person/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public PersonResponseDto addAddress(@PathVariable long id, @RequestBody AddressDto address)
            throws ConflictException {

        log.info(API_CALL_MESSAGE + "Add a new address");

        Address addressModel = PersonController.convertRequestDto(address);

        Person person = this.personService.addAddress(id, addressModel);

        log.info(SUCCESS_MESSAGE + "Added new address to person with id {}", id);
        return this.convertModel(person);
    }

    @DeleteMapping(path = "/person/{id}")
    public void delete(@PathVariable long id) {
        log.info(API_CALL_MESSAGE + "Delete a person");

        this.personService.delete(id);
    }

    public Person convertRequestDto(PersonRequestDto dto) throws NotFoundException {
        Person person = new Person();

        Optional<Long> company = dto.getCompany();

        person.setFirstname(dto.getFirstname());
        person.setLastname(dto.getLastname());

        if (company.isPresent()) {
            Company existingCompany = this.companyService.getById(company.get());

            person.setCompany(existingCompany);
        }

        return person;
    }

    public static PersonResponseDto convertModel(Person person) {
        PersonResponseDto dto = new PersonResponseDto();

        Company company = person.getCompany();

        dto.setFirstname(person.getFirstname());
        dto.setLastname(person.getLastname());

        if (Objects.nonNull(person.getAddresses())) {
            dto.setAddresses(
                    person.getAddresses().stream().map(PersonController::convertModel).collect(Collectors.toList())
            );
        }

        if (Objects.nonNull(company)) {
            dto.setCompany(company.getId());
        }

        return dto;
    }

    public static AddressDto convertModel(Address address) {
        AddressDto dto = new AddressDto();

        dto.setCity(address.getCity());
        dto.setCountry(address.getCountry());
        dto.setStreet(address.getStreet());
        dto.setStreetNumber(address.getStreetNumber());
        dto.setZipCode(address.getZipCode());

        return dto;
    }

    public static Address convertRequestDto(AddressDto dto) {
        Address address = new Address();

        address.setCity(dto.getCity());
        address.setCountry(dto.getCountry());
        address.setStreet(dto.getStreet());
        address.setStreetNumber(dto.getStreetNumber());
        address.setZipCode(dto.getZipCode());

        return address;
    }
}