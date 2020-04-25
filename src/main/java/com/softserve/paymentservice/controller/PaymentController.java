package com.softserve.paymentservice.controller;

import com.softserve.paymentservice.service.CardService;
import com.softserve.paymentservice.service.InvoiceService;
import com.softserve.paymentservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
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
        if (userService.isUserCreated(userId)) {
            if (!cardService.getAllCards(userService.getUser(userId)).isEmpty()) {
                if (!invoiceService.hasUnpaidInvoice(userService.getUser(userId))) {
                    return ResponseEntity.ok(true);
                } else if (invoiceService.hasUnpaidInvoice(userService.getUser(userId))) {
                    return ResponseEntity.ok(true);
                }
            }
        }
        return ResponseEntity.ok(false);
    }
}