package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.exception.CardNotFoundException;
import com.softserve.paymentservice.exception.CardParametersException;
import com.softserve.paymentservice.model.User;
import com.softserve.paymentservice.repository.UserRepository;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {
    PaymentService paymentServiceStripe;
    private final UserRepository userRepository;

    public boolean addCardToUser(CardDto cardDto, UUID userId) throws CardParametersException {
        try {
            User user = userRepository.findUserByUserId(userId)
                    .orElse(userRepository.saveAndFlush(new User(userId, paymentServiceStripe.createCustomer(userId))));
            return paymentServiceStripe.addCard(user.getCustomerId(), cardDto);
        } catch (StripeException e) {
            throw new CardParametersException();
        }
    }

    public Map<String, String> getAllCards(UUID userId) throws CardNotFoundException { //todo is it okay to return map?
        try {
            User user = userRepository.findUserByUserId(userId).get(); //.orElseThrow(UserNotFoundException::new); //todo how?
            return paymentServiceStripe.getAllCards(user.getCustomerId());
        } catch (StripeException e) {
            throw new CardNotFoundException();
        }
    }

    public String setDefaultCard(UUID userId, int last4NumbersFromCard) throws CardNotFoundException {
        try {
            User user = userRepository.findUserByUserId(userId).get(); //.orElseThrow(UserNotFoundException::new); //todo how?
            if (paymentServiceStripe.setDefaultCard(user.getCustomerId(), String.valueOf(last4NumbersFromCard)) != null) {
                return paymentServiceStripe.setDefaultCard(user.getCustomerId(),  String.valueOf(last4NumbersFromCard));
            } else {
                throw new CardNotFoundException();
            }
        } catch (StripeException e) {
            throw new CardNotFoundException();
        }
    }


    public String deleteCard(UUID userId, int last4NumbersFromCard) throws CardNotFoundException {
        try {
            User user = userRepository.findUserByUserId(userId).get(); //.orElseThrow(UserNotFoundException::new); //todo how?
            if (paymentServiceStripe.deleteCard(user.getCustomerId(),  String.valueOf(last4NumbersFromCard)) != null) {
                return paymentServiceStripe.setDefaultCard(user.getCustomerId(),  String.valueOf(last4NumbersFromCard));
            } else {
                throw new CardNotFoundException();
            }
        } catch (StripeException e) {
            throw new CardNotFoundException();
        }
    }
}
