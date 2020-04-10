package com.softserve.paymentservice.util;

import com.softserve.paymentservice.service.CardService;
import com.softserve.paymentservice.service.InvoiceService;
import com.softserve.paymentservice.service.UserService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@RequiredArgsConstructor
public class UserSolvencyValidator implements ConstraintValidator<UserSolvencyValidation, UUID> {

    private final CardService cardService;
    private final InvoiceService invoiceService;
    private final UserService userService;


    @Override
    public boolean isValid(UUID userId, ConstraintValidatorContext constraintValidatorContext) {
        return userService.isUserCreated(userId) &&
                !cardService.getAllCards(userService.getUser(userId)).isEmpty() &&
                (!invoiceService.hasUnpaidInvoice(userService.getUser(userId)) ||
                        invoiceService.payUnpaidInvoice(userService.getUser(userId)));
    }
}
