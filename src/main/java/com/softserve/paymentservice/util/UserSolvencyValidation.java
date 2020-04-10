package com.softserve.paymentservice.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserSolvencyValidator.class)
public @interface UserSolvencyValidation {
    
    String message() default "User has unpaid invoice.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
