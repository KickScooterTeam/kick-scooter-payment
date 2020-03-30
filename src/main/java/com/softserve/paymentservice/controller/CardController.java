package com.softserve.paymentservice.controller;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.model.User;
import com.softserve.paymentservice.service.CardService;
import com.softserve.paymentservice.service.InvoiceService;
import com.softserve.paymentservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    final CardService cardService;
    final UserService userService;
    final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<String> addCard(@RequestBody CardDto cardDto) {
        if (cardService.addCardToUser(cardDto, userService.getOrCreateUser(cardDto.getUserUUID()))) {
            return ResponseEntity.ok("card was successful added");
        }
        return ResponseEntity.status(403).build();
    }

    @PutMapping("/default")
    public ResponseEntity<CardDto> setDefaultCard(@RequestBody CardDto cardDto) {
        return ResponseEntity.ok(cardService.setDefaultCard(userService.getUser(cardDto.getUserUUID()), cardDto.getLast4()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CardDto>> getAllCard(@RequestParam(name = "userId") UUID userId) {
        return ResponseEntity.ok(cardService.getAllCards(userService.getUser(userId)));
    }

    @DeleteMapping
    public ResponseEntity<CardDto> deleteCard(@RequestBody CardDto cardDto) {
        User user = userService.getUser(cardDto.getUserUUID());
        if (invoiceService.getUnpaidInvoices(user).isEmpty()) {
            return ResponseEntity.ok(cardService.deleteCard(user, cardDto.getLast4()));
        }
        return ResponseEntity.status(403).build();
    }


}
