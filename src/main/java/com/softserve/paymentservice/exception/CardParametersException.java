package com.softserve.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Card parameters incorrect")
public class CardParametersException extends RuntimeException {
    public CardParametersException(String message) {
        super(message);
    }
}
