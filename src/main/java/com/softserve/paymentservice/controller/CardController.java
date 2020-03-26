package com.softserve.paymentservice.controller;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.service.CardService;
import com.softserve.paymentservice.service.InvoiceService;
import com.softserve.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("card")
public class CardController {

    private final CardService cardService;
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;

    @PutMapping("/default")
    ResponseEntity<String> setDefaultCard(@RequestBody CardDto cardDto) {


        return ResponseEntity.ok("replace");
    }

    @PostMapping("/new")
    ResponseEntity<String> addCard(@RequestBody CardDto cardDto) { //todo + check card
        return ResponseEntity.ok("replace");
    }

    @GetMapping("/all")
    ResponseEntity<String> getAllCard(@RequestBody CardDto cardDto) {
        return ResponseEntity.ok("replace");
    }

    @PostMapping("/remove")
    ResponseEntity<String> removeCard(@RequestBody CardDto cardDto) { //todo + check card
        return ResponseEntity.ok("replace");
    }


}
