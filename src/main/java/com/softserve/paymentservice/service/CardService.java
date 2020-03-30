package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final PaymentService paymentService;


    public boolean addCardToUser(CardDto cardDto, User user) {
        return paymentService.addCard(user, cardDto);
    }

    public CardDto setDefaultCard(User user, int last4NumbersFromCard) {
        return paymentService.setDefaultCard(user, String.valueOf(last4NumbersFromCard));
    }

    public List<CardDto> getAllCards(User user) {
        return paymentService.getAllCards(user);
    }

    public CardDto deleteCard(User user, int last4NumbersFromCard) {
        return paymentService.deleteCard(user, String.valueOf(last4NumbersFromCard));
    }
}
