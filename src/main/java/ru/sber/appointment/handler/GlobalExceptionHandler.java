package ru.sber.appointment.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleResourceAccessException(ResourceAccessException ex) {
        return "serverError";
    }
}
