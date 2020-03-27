package com.softserve.paymentservice.controller;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.service.CardService;
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

    @PutMapping("/default")
    public ResponseEntity<CardDto> setDefaultCard(@RequestBody CardDto cardDto) {
        return ResponseEntity.ok(cardService.setDefaultCard(cardDto.getUserUUID(), cardDto.getLast4()));
    }

    @PostMapping
    public ResponseEntity<String> addCard(@RequestBody CardDto cardDto) {
        if (cardService.addCardToUser(cardDto)) {
            return ResponseEntity.ok("card was successful added");
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<CardDto>> getAllCard(@RequestParam(name = "userId") UUID userId) {
        return ResponseEntity.ok(cardService.getAllCards(userId));
    }

    @DeleteMapping
    public ResponseEntity<CardDto> removeCard(@RequestBody CardDto cardDto) {
        cardService.deleteCard(cardDto.getUserUUID(), cardDto.getLast4());
        return ResponseEntity.ok(cardService.deleteCard(cardDto.getUserUUID(), cardDto.getLast4()));
    }


}
