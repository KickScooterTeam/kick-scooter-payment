package com.softserve.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Incorrect user's parameters.")
public class UserCreationException extends RuntimeException {
    public UserCreationException(String message) {
        super(message);
    }
}



