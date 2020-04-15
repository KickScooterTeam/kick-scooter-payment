package com.softserve.paymentservice.controller;

import com.softserve.paymentservice.service.CardService;
import com.softserve.paymentservice.service.InvoiceService;
import com.softserve.paymentservice.service.UserService;
import com.softserve.paymentservice.util.UserSolvencyValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Boolean> checkUserBeforeTrip(@UserSolvencyValidation @PathVariable UUID userId) {
        log.info("User {} was checked (from )", userId);
        return ResponseEntity.ok(true);
    }
}