package com.springcontext.springexceptionhandling.services;

import com.springcontext.springexceptionhandling.entities.Company;
import com.springcontext.springexceptionhandling.entities.Person;
import com.springcontext.springexceptionhandling.exceptions.ConflictException;
import com.springcontext.springexceptionhandling.exceptions.NotFoundException;
import com.springcontext.springexceptionhandling.repositories.CompanyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final PersonService personService;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, PersonService personService) {
        this.companyRepository = companyRepository;
        this.personService = personService;
    }

    public Company create(Company company) {
        log.info("Adding a new company - name {}", company.getName());

        return this.companyRepository.save(company);
    }

    public Company getById(long id) throws NotFoundException {
        log.info("Trying to fetch Company with id {}", id);

        Optional<Company> company = this.companyRepository.findById(id);

        if (company.isPresent()) {
            return company.get();
        }

        log.error("No company found with id {}", id);
        throw new NotFoundException("This company does not exist");
    }

    public Company addEmployee(long companyId, long personId) throws NotFoundException, ConflictException {
        Company company = this.getById(companyId);

        Person person;

        try {
            person = this.personService.getById(personId);
        } catch (NotFoundException e) {
            log.error("No person found with ID {}", personId);
            throw new ConflictException(e.getMessage());
        }

        log.info("Adding Person {} in Company {}", personId, companyId);

        company.addEmployee(person);
        person.setCompany(company);

        this.companyRepository.save(company);

        return company;
    }

    public void delete(long id) {
        this.companyRepository.deleteById(id);
    }
}