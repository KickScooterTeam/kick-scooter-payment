package com.softserve.paymentservice.controller;

import com.softserve.paymentservice.dto.PaymentInfoDto;
import com.softserve.paymentservice.exception.InvoiceNotFoundException;
import com.softserve.paymentservice.model.Invoice;
import com.softserve.paymentservice.service.AmountCalculator;
import com.softserve.paymentservice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("invoice")
public class InvoiceController {

    private final AmountCalculator amountCalculator;
    private final InvoiceService invoiceService;

    @PostMapping("/new")
    ResponseEntity<Invoice> payInvoice(@RequestBody PaymentInfoDto paymentInfoDto) throws InvoiceNotFoundException {
        return ResponseEntity.ok(invoiceService.createInvoice(amountCalculator.calculateAmount(paymentInfoDto), paymentInfoDto.getUserid()));
    }

    @GetMapping("/all")
    ResponseEntity<List<Invoice>> getAllinvoices(@RequestParam(name = "userId") UUID userId) {
        return ResponseEntity.ok(invoiceService.getInvoices(userId));
    }
}