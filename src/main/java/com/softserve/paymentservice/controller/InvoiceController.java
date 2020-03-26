package com.softserve.paymentservice.controller;

import com.softserve.paymentservice.dto.PaymentInfoDto;
import com.softserve.paymentservice.service.AmountCalculator;
import com.softserve.paymentservice.service.CardService;
import com.softserve.paymentservice.service.InvoiceService;
import com.softserve.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("invoice")
public class InvoiceController {

    private final AmountCalculator amountCalculator;
    private final CardService cardService;
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;


    @GetMapping("/all")
    ResponseEntity<String> getAllinvoices(@RequestParam(name = "userId") UUID userId) {


        return ResponseEntity.ok("replace");
    }

    @PostMapping("/new")
    ResponseEntity<String> payInvoice(@RequestBody PaymentInfoDto paymentInfoDto) {


        return ResponseEntity.ok("replace");
    }

    @PostMapping("/admin/refund")
    ResponseEntity<String> refundInvoice(@RequestParam(name = "userId") UUID userId) {
        return ResponseEntity.ok("replace");
    }
}