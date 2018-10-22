package com.springcontext.springexceptionhandling.controllers.dto.person;

import com.springcontext.springexceptionhandling.controllers.dto.address.AddressDto;
import lombok.Data;

import java.util.List;

@Data
public class PersonResponseDto {

    private String firstname;

    private String lastname;

    private List<AddressDto> addresses;

    private Long company;
}
