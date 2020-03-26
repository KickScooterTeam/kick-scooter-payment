package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.CardDto;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CardService {

    @Value("${stripe.key.secret}")
    private String stripeSecretKey;


    public String addCardToUser(CardDto cardDto, String customerId) throws StripeException {
        Map<String, Object> cardParameters = new HashMap<>();
        cardParameters.put("number", cardDto.getCardNumber());
        cardParameters.put("exp_month", cardDto.getMonth());
        cardParameters.put("exp_year", cardDto.getYear());
        cardParameters.put("cvc", cardDto.getCvc());

        Customer customer = Customer.retrieve(customerId);

        Map<String, Object> tokenParameters = new HashMap<>();
        tokenParameters.put("card", cardParameters);

        Token token = Token.create(tokenParameters);
        Map<String, Object> source = new HashMap<String, Object>();
        source.put("source", token.getId());

        customer.getSources().create(source).getId();
        return customer.getSources().create(source).getId();
    }


    public String chaeckCard(CardDto cardDto, String customerId) throws StripeException {
        Map<String, Object> cardParameters = new HashMap<>();
        cardParameters.put("number", cardDto.getCardNumber());
        cardParameters.put("exp_month", cardDto.getMonth());
        cardParameters.put("exp_year", cardDto.getYear());
        cardParameters.put("cvc", cardDto.getCvc());

        Customer customer = Customer.retrieve(customerId);

        Map<String, Object> tokenParameters = new HashMap<>();
        tokenParameters.put("card", cardParameters);

        Token token = Token.create(tokenParameters);
        Map<String, Object> source = new HashMap<String, Object>();
        source.put("source", token.getId());

        customer.getSources().create(source).getId();
        return customer.getSources().create(source).getId();
    }




}
