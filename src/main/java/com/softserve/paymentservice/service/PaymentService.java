package com.softserve.paymentservice.service;

import com.softserve.paymentservice.model.Card;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Refund;
import com.stripe.model.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {

    @Value("${stripe.key.secret}")
    private String stripeSecretKey;


    public Boolean pay(int amount, Card card, String cvc) throws StripeException {
        return getCharge(amount, card, cvc).getPaid();
    }

    public Boolean checkingPayment(Card card, String cvc) throws StripeException {
        Charge charge = getCharge(100, card, cvc);
        Refund refund = new Refund();
        if (charge.getPaid()) {
            boolean successfulRefund = false;
            while (!successfulRefund) {
                Map<String, Object> refundParameters = new HashMap<>();
                refundParameters.put("charge", charge.getId());
                refund = Refund.create(refundParameters);
                if (refund.getStatus().equalsIgnoreCase("succeeded")) {
                    successfulRefund = true;
                    return true;
                }
            }
        }
        return false;
    }

    private Charge getCharge(int amount, Card card, String cvc) throws StripeException {

        String customerId = getStripeCustomerId(card.getUserId());
        String cardId = getSourceId(customerId, getCardMap(card, cvc));

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", amount);
        chargeParams.put("currency", "EUR");
        chargeParams.put("customer", customerId);
        chargeParams.put("card", cardId);

        Charge charge = Charge.create(chargeParams);
        return charge;
    }


    private String getStripeCustomerId(UUID userId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", userId);
        Customer customer = Customer.create(userMap);
        return customer.getId();
    }

    private Map<String, Object> getCardMap(Card card, String cvc) {
        Map<String, Object> cardMap = new HashMap<>();
        cardMap.put("number", card.getCardNumber());
        cardMap.put("exp_month", card.getExpMonth());
        cardMap.put("exp_year", card.getExpYear());
        cardMap.put("cvc", cvc);
        return cardMap;
    }


    private String getSourceId(String customerId, Map<String, Object> cardMap) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        Map<String, Object> tokenParameters = new HashMap<>();
        tokenParameters.put("card", cardMap);
        Token token = Token.create(tokenParameters);
        Map<String, Object> source = new HashMap<String, Object>();
        source.put("source", token.getId());
        return customer.getSources().create(source).getId();

    }


}
