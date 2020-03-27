package com.softserve.paymentservice.controller;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.exception.CardNotFoundException;
import com.softserve.paymentservice.exception.CardParametersException;
import com.softserve.paymentservice.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("card")
public class CardController {

    private final CardService cardService;

    @PutMapping("/default")
    ResponseEntity<String> setDefaultCard(@RequestBody CardDto cardDto) throws CardNotFoundException {
        return ResponseEntity.ok(cardService.setDefaultCard(cardDto.getUserUUID(), cardDto.getLast4()));
    }

    @PostMapping("/new")
    ResponseEntity<String> addCard(@RequestBody CardDto cardDto) throws CardParametersException {
        if (cardService.addCardToUser(cardDto, cardDto.getUserUUID())) {
            return ResponseEntity.ok("card was successful added");
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/all")
    ResponseEntity<Map<String, String>> getAllCard(@RequestParam(name = "userId") UUID userId) throws CardNotFoundException {
        return ResponseEntity.ok(cardService.getAllCards(userId));
    }

    @PostMapping("/delete")
    ResponseEntity<String> removeCard(@RequestBody CardDto cardDto) throws CardNotFoundException {
        cardService.deleteCard(cardDto.getUserUUID(), cardDto.getLast4());
        return ResponseEntity.ok(cardService.deleteCard(cardDto.getUserUUID(), cardDto.getLast4()));
    }


}
