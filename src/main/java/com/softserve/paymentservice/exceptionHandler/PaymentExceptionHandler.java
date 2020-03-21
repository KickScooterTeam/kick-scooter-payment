package com.softserve.paymentservice.exceptionHandler;

import com.softserve.paymentservice.model.ErrorResponse;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice// todo @responseStatus above mycustomEcxception --> we don't need this
public class PaymentExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(StripeException.class)
    public ResponseEntity<ErrorResponse> stripeException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .time(LocalDateTime.now())
                .status(500)
                .error(e.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //    @ExceptionHandler(value = {CardException.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<ErrorResponse> badRequest(Exception cardEx) {
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .status(Integer.parseInt(cardEx.getCode()))
//                .error(cardEx.getMessage())
//                .time(LocalDateTime.now())
//                .build();
//        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//    @ExceptionHandler(NullPointerException.class)
//    public ResponseEntity<ErrorResponse> justException(Exception e) {
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .time(LocalDateTime.now())
//                .status(500)
//                .error(e.getMessage())
//                .build();
//        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
    /*
     Use Stripe's library to make requests...
     (CardException e) Since it's a decline, CardException will be caught
     (RateLimitException e)  Too many requests made to the API too quickly
     (InvalidRequestException e)  Invalid parameters were supplied to Stripe's API
     (AuthenticationException e)  Authentication with Stripe's API failed  (maybe you changed API keys recently)
     (APIConnectionException e)  Network communication with Stripe failed
     (StripeException e)   Display a very generic error to the user, and maybe send yourself an email
     (Exception e)  Something else happened, completely unrelated to Stripe
     */

}
