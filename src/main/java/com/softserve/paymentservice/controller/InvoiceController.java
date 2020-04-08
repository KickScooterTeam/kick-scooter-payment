package com.softserve.paymentservice.controller;

import com.softserve.paymentservice.dto.InvoiceDto;
import com.softserve.paymentservice.service.InvoiceService;
import com.softserve.paymentservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invoices")
@Slf4j
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final UserService userService;



    
    @GetMapping("/{userId}")
    public ResponseEntity<List<InvoiceDto>> getAlInvoices(@PathVariable UUID userId) {
        return ResponseEntity.ok(invoiceService.getInvoices(userService.getUser(userId)));
    }

    @GetMapping("/{userId}/unpaid")
    public ResponseEntity<List<InvoiceDto>> getUnpaidInvoices(@PathVariable UUID userId) {
        return ResponseEntity.ok(invoiceService.getUnpaidInvoices(userService.getUser(userId)));
    }

    @PutMapping("/{invoiceId}")
    public ResponseEntity<InvoiceDto> payUnpaidInvoice(@PathVariable String invoiceId) {
        return ResponseEntity.ok(invoiceService.payUnpaidInvoice(invoiceId));
    }


}