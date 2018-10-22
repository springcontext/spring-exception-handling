package com.springcontext.springexceptionhandling.repositories;

import com.springcontext.springexceptionhandling.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}