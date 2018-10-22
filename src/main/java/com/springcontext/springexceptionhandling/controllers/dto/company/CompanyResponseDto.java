package com.springcontext.springexceptionhandling.controllers.dto.company;

import com.springcontext.springexceptionhandling.controllers.dto.person.PersonResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class CompanyResponseDto {

    private String name;

    private List<PersonResponseDto> employees;
}
