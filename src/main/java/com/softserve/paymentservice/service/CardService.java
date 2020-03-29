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
public class CardService  {

    private final PaymentService paymentService;
    private final UserRepository userRepository;


    public boolean addCardToUser(CardDto cardDto) {
        User user = userRepository.findById(cardDto.getUserUUID())
                .orElseGet(()->userRepository.save(new User(cardDto.getUserUUID(), paymentService.createUser(cardDto.getUserUUID())))); //todo in UserService createUser / findUser getOrCreate method --> for
        return paymentService.addCard(user.getCustomerId(), cardDto);
    }

    public List<CardDto> getAllCards(UUID userId) {
        User user = userRepository.findAppUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
        return paymentService.getAllCards(user.getCustomerId());

    }

    public CardDto setDefaultCard(UUID userId, int last4NumbersFromCard) {
        User user = userRepository.findAppUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
        if (paymentService.setDefaultCard(user.getCustomerId(), String.valueOf(last4NumbersFromCard)) != null) {
            return paymentService.setDefaultCard(user.getCustomerId(), String.valueOf(last4NumbersFromCard));
        } else {
            throw new CardNotFoundException("Card cannot be set as default because it was not found.");
        }

    }


    public CardDto deleteCard(UUID userId, int last4NumbersFromCard) throws CardNotFoundException {
        User user = userRepository.findAppUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
        if (paymentService.deleteCard(user.getCustomerId(), String.valueOf(last4NumbersFromCard)) != null) {
            return paymentService.setDefaultCard(user.getCustomerId(), String.valueOf(last4NumbersFromCard));
        } else {
            throw new CardNotFoundException("Card cannot be deleted because it was not found.");
        }

    }
}
