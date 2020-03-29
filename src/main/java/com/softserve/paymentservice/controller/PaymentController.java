package com.softserve.paymentservice.controller;

import com.softserve.paymentservice.service.CardService;
import com.softserve.paymentservice.service.InvoiceService;
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

    @PostMapping("/user-validation")
    public ResponseEntity<Boolean> checkUserBeforeTrip(@RequestParam(name = "userId") UUID userId) {//todo check if cust id created
        if (!cardService.getAllCards(userId).isEmpty() && invoiceService.getUnpaidInvoices(userId).isEmpty()) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(403).build();
    }
}