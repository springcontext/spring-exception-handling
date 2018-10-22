package com.springcontext.springexceptionhandling.controllers.dto.person;

import lombok.Data;

import java.util.Optional;

@Data
public class PersonRequestDto {

    private String firstname;

    private String lastname;

    private Optional<Long> company;
}