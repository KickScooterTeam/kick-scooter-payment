package com.softserve.paymentservice.controller;

import com.softserve.paymentservice.service.CardService;
import com.softserve.paymentservice.service.InvoiceService;
import com.softserve.paymentservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final CardService cardService;
    private final InvoiceService invoiceService;
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}/user-solvency")
    public ResponseEntity<Boolean> checkUserBeforeTrip(@PathVariable UUID userId) {
        return userService.isUserCreated(userId) &&
                !cardService.getAllCards(userService.getUser(userId)).isEmpty() &&
                (!invoiceService.hasUnpaidInvoice(userService.getUser(userId)) ||
                        invoiceService.payUnpaidInvoice(userService.getUser(userId)))
                ? ResponseEntity.ok(true) : ResponseEntity.ok(false);
    }

}