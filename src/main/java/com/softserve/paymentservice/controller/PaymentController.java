package com.softserve.paymentservice.controller;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.service.AmountCalculator;
import com.softserve.paymentservice.service.CardService;
import com.softserve.paymentservice.service.InvoiceService;
import com.softserve.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("payment")
public class PaymentController {

    private final AmountCalculator amountCalculator;
    private final CardService cardService;
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;

    @PostMapping("/validate-user")
    ResponseEntity<Boolean> addCard(@RequestParam(name = "userId") UUID userId) {
                return ResponseEntity.ok(true);
    }
}