package com.springcontext.springexceptionhandling.exceptions.dto;

import lombok.Data;

@Data
public class HttpExceptionDto {

    private String method;

    private int code;

    private String path;

    private String message;
}
