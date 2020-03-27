package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.exception.CardNotFoundException;
import com.softserve.paymentservice.exception.UserNotFoundException;
import com.softserve.paymentservice.model.AppUser;
import com.softserve.paymentservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {

    private final StripePaymentService paymentServiceStripe;
    private final UserRepository userRepository;


    public boolean addCardToUser(CardDto cardDto) {
        AppUser appUser = userRepository.findById(cardDto.getUserUUID())
                .orElse(userRepository.save(new AppUser(cardDto.getUserUUID(), paymentServiceStripe.createCustomer(cardDto.getUserUUID()))));
        System.out.println(appUser.getCustomerId() + "<------=========");
        return paymentServiceStripe.addCard(appUser.getCustomerId(), cardDto);
    }

    public List<CardDto> getAllCards(UUID userId) {
        AppUser appUser = userRepository.findAppUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
        return paymentServiceStripe.getAllCards(appUser.getCustomerId());

    }

    public CardDto setDefaultCard(UUID userId, int last4NumbersFromCard) {
        AppUser appUser = userRepository.findAppUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
        if (paymentServiceStripe.setDefaultCard(appUser.getCustomerId(), String.valueOf(last4NumbersFromCard)) != null) {
            return paymentServiceStripe.setDefaultCard(appUser.getCustomerId(), String.valueOf(last4NumbersFromCard));
        } else {
            throw new CardNotFoundException("Card cannot be set as default because it was not found.");
        }

    }


    public CardDto deleteCard(UUID userId, int last4NumbersFromCard) throws CardNotFoundException {
        AppUser appUser = userRepository.findAppUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
        if (paymentServiceStripe.deleteCard(appUser.getCustomerId(), String.valueOf(last4NumbersFromCard)) != null) {
            return paymentServiceStripe.setDefaultCard(appUser.getCustomerId(), String.valueOf(last4NumbersFromCard));
        } else {
            throw new CardNotFoundException("Card cannot be deleted because it was not found.");
        }

    }
}
