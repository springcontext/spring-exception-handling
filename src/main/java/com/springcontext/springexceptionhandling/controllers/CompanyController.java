package com.springcontext.springexceptionhandling.controllers;

import com.springcontext.springexceptionhandling.controllers.dto.company.CompanyRequestDto;
import com.springcontext.springexceptionhandling.controllers.dto.company.CompanyResponseDto;
import com.springcontext.springexceptionhandling.controllers.dto.person.PersonResponseDto;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CompanyController {

    private static final String API_CALL_MESSAGE = "New API call: ";
    private static final String FAILURE_MESSAGE = "Failure: ";
    private static final String SUCCESS_MESSAGE = "Success: ";

    private final CompanyService companyService;

    private final PersonService personService;

    @Autowired
    public CompanyController(CompanyService companyService, PersonService personService) {
        this.companyService = companyService;
        this.personService = personService;
    }

    @PostMapping(path = "/company", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CompanyResponseDto create(@RequestBody CompanyRequestDto company) {

        log.info(API_CALL_MESSAGE + "Create a company");

        if (Objects.isNull(company)) {
            log.error(FAILURE_MESSAGE + "The request body is null");
            return null;
        }

        Company createdCompany = this.companyService.create(CompanyController.convertRequestDto(company));

        log.info(SUCCESS_MESSAGE + "New company created!");

        return CompanyController.convertModel(createdCompany);
    }

    @GetMapping(path = "/company/{id}/people", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonResponseDto> getEmployees(@PathVariable long id) throws NotFoundException {

        log.info(API_CALL_MESSAGE + "Get company employees");

        Company company = this.companyService.getById(id);

        log.info(SUCCESS_MESSAGE + "Fetching all the employees!");

        return company.getEmployees().stream().map(PersonController::convertModel).collect(Collectors.toList());
    }

    @PutMapping(
            path = "/company/{company_id}/people/{person_id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public CompanyResponseDto addEmployee(
            @PathVariable("company_id") long companyId,
            @PathVariable("person_id") long personId
    ) throws NotFoundException, ConflictException {

        log.info(API_CALL_MESSAGE + "Add an employee in a company");

        Company company = this.companyService.addEmployee(companyId, personId);

        return CompanyController.convertModel(company);
    }

    @DeleteMapping(path = "/company/{id}")
    public void delete(@PathVariable long id) throws NotFoundException {
        log.info(API_CALL_MESSAGE + "Delete a company");

        this.companyService.delete(id);
    }

    public static Company convertRequestDto(CompanyRequestDto dto) {

        Company company = new Company();

        company.setName(dto.getName());

        return company;
    }

    public static CompanyResponseDto convertModel(Company company) {

        CompanyResponseDto dto = new CompanyResponseDto();

        dto.setName(company.getName());

        List<Person> employees = company.getEmployees();

        if (Objects.nonNull(employees)) {
            dto.setEmployees(
                    employees.stream().map(PersonController::convertModel).collect(Collectors.toList())
            );
        }

        return dto;
    }
}
