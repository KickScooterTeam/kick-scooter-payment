package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.exception.CardNotFoundException;
import com.softserve.paymentservice.exception.UserNotFoundException;
import com.softserve.paymentservice.model.User;
import com.softserve.paymentservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {
    final StripePaymentService paymentServiceStripe;
    private final UserRepository userRepository;

    public boolean addCardToUser(CardDto cardDto, UUID userId) {
        User user = userRepository.findUserByUserId(userId)
                .orElse(userRepository.save(new User(userId, paymentServiceStripe.createCustomer(userId))));
        return paymentServiceStripe.addCard(user.getCustomerId(), cardDto);
    }

    public List<CardDto> getAllCards(UUID userId) throws CardNotFoundException {
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
        return paymentServiceStripe.getAllCards(user.getCustomerId());

    }

    public CardDto setDefaultCard(UUID userId, int last4NumbersFromCard) throws CardNotFoundException {
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
        if (paymentServiceStripe.setDefaultCard(user.getCustomerId(), String.valueOf(last4NumbersFromCard)) != null) {
            return paymentServiceStripe.setDefaultCard(user.getCustomerId(), String.valueOf(last4NumbersFromCard));
        } else {
            throw new CardNotFoundException("Card cannot be set as default because it was not found.");
        }

    }


    public CardDto deleteCard(UUID userId, int last4NumbersFromCard) throws CardNotFoundException {
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
        if (paymentServiceStripe.deleteCard(user.getCustomerId(), String.valueOf(last4NumbersFromCard)) != null) {
            return paymentServiceStripe.setDefaultCard(user.getCustomerId(), String.valueOf(last4NumbersFromCard));
        } else {
            throw new CardNotFoundException("Card cannot be deleted because it was not found.");
        }

    }
}
