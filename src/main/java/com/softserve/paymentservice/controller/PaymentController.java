package com.softserve.paymentservice.controller;

import com.softserve.paymentservice.service.CardService;
import com.softserve.paymentservice.service.InvoiceService;
import com.softserve.paymentservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final CardService cardService;
    private final InvoiceService invoiceService;
    private final UserService userService;

    @PostMapping("/user-validation")
    public ResponseEntity<Boolean> checkUserBeforeTrip(@RequestParam(name = "userId") UUID userId) {
        if (userService.isUserCreated(userId)) {
            if (!cardService.getAllCards(userService.getUser(userId)).isEmpty()
                    && invoiceService.getUnpaidInvoices(userService.getUser(userId)).isEmpty()) {
                return ResponseEntity.ok(true);
            }
        }
        return ResponseEntity.ok(false);
    }
}