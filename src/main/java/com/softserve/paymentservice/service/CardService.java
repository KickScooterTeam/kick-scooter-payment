package com.softserve.paymentservice.service;

import com.softserve.paymentservice.exception.CardNotFoundException;
import com.softserve.paymentservice.model.Card;
import com.softserve.paymentservice.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public List<Card> getCardsByUserId(UUID userId) {
        return cardRepository.findByUserId(userId);
    }

    public Card addCardToUser(Card card) {
        cardRepository.save(card);
        return card;
    }

    public Card getUserCardByNumber(String cardNumber, UUID userId) {
        Card card = cardRepository.findCardByCardNumberAndUserId(cardNumber,userId).get();
        return card;
    }

    public Card setWorkingStatus(String cardNumber, UUID userId, Boolean workingStatus) throws CardNotFoundException {
        Card card = cardRepository.findCardByCardNumberAndUserId(cardNumber,userId).orElseThrow(CardNotFoundException::new);
        card.setWorking(workingStatus);
        cardRepository.save(card);
        return card;
    }


}
