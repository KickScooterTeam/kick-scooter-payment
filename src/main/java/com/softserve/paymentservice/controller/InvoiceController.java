package com.softserve.paymentservice.controller;

import com.softserve.paymentservice.dto.InvoiceDto;
import com.softserve.paymentservice.dto.PaymentInfoDto;
import com.softserve.paymentservice.exception.InvoiceNotFoundException;
import com.softserve.paymentservice.model.Invoice;
import com.softserve.paymentservice.service.AmountCalculator;
import com.softserve.paymentservice.service.InvoiceService;
import com.softserve.paymentservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invoices")
@Slf4j
public class InvoiceController {

    private final AmountCalculator amountCalculator;
    private final InvoiceService invoiceService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<InvoiceDto> createInvoice(@RequestBody PaymentInfoDto paymentInfoDto){
        log.info(paymentInfoDto.toString());
        return ResponseEntity.ok(invoiceService.createInvoice(amountCalculator.calculateAmount(paymentInfoDto),
                userService.getUser(paymentInfoDto.getUserId())));
    }

    @GetMapping("/all")
    public ResponseEntity<List<InvoiceDto>> getAlInvoices(@RequestParam(name = "userId") UUID userId) {
        return ResponseEntity.ok(invoiceService.getInvoices( userService.getUser(userId)));
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<InvoiceDto>> getUnpaidInvoices(@RequestParam(name = "userId") UUID userId) {
        return ResponseEntity.ok(invoiceService.getUnpaidInvoices( userService.getUser(userId)));
    }

    @PutMapping
    public ResponseEntity<InvoiceDto> payUnpaidInvoice(@RequestParam(name = "invoiceId") String invoiceId){
        return ResponseEntity.ok(invoiceService.payUnpaidInvoice(invoiceId));
    }


}