package com.springcontext.springexceptionhandling.controllers.handlers;

import com.springcontext.springexceptionhandling.exceptions.ConflictException;
import com.springcontext.springexceptionhandling.exceptions.NotFoundException;
import com.springcontext.springexceptionhandling.exceptions.dto.HttpExceptionDto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class HttpExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public HttpExceptionDto handleConflict(Exception e, HttpServletRequest request) {

        HttpExceptionDto httpExceptionDto = new HttpExceptionDto();

        httpExceptionDto.setCode(409);
        httpExceptionDto.setMessage(e.getMessage());
        httpExceptionDto.setPath(request.getRequestURI());
        httpExceptionDto.setMethod(request.getMethod());

        return httpExceptionDto;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public HttpExceptionDto handleNotFound(Exception e, HttpServletRequest request) {

        HttpExceptionDto httpExceptionDto = new HttpExceptionDto();

        httpExceptionDto.setCode(404);
        httpExceptionDto.setMessage(e.getMessage());
        httpExceptionDto.setPath(request.getRequestURI());
        httpExceptionDto.setMethod(request.getMethod());

        return httpExceptionDto;
    }
}
