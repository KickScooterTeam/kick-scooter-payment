package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.CardDto;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Value("${stripe.key.secret}")
    private String stripeSecretKey;

    public String createCustomer(UUID userId) throws StripeException {
        Map<String, Object> customerParameter = new HashMap<>();
        customerParameter.put("name", "testingTest");
        return Customer.create(customerParameter).getId();
    }

    public com.stripe.model.Invoice createInvoice(int amount, String customerId) throws StripeException {
        Map<String, Object> invoiceItemParams = new HashMap<String, Object>();
        invoiceItemParams.put("customer", customerId);
        invoiceItemParams.put("amount", amount);
        invoiceItemParams.put("currency", "EUR");
        invoiceItemParams.put("description", "date  " + Instant.now());
        InvoiceItem.create(invoiceItemParams);

        Map<String, Object> invoiceParams = new HashMap<String, Object>();
        invoiceParams.put("customer", customerId);
        invoiceParams.put("auto_advance", false);
        invoiceParams.put("collection_method", "charge_automatically");
        com.stripe.model.Invoice invoiceStripe = com.stripe.model.Invoice.create(invoiceParams);
        invoiceStripe.pay();
        return invoiceStripe;
    }


    public boolean addCard(String customerId, CardDto cardDto) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        Map<String, Object> cardParameters = new HashMap<String, Object>();

        cardParameters.put("number", cardDto.getCardNumber());
        cardParameters.put("exp_month", cardDto.getMonth());
        cardParameters.put("exp_year", cardDto.getYear());
        cardParameters.put("cvc", cardDto.getCvc());

        Map<String, Object> tokenParameters = new HashMap<String, Object>();
        tokenParameters.put("card", cardParameters);
        Token token = Token.create(tokenParameters);

        Map<String, Object> source = new HashMap<String, Object>();
        source.put("source", token.getId());
        return checkCard(customerId, customer.getSources().create(source).getId());
    }

    private boolean checkCard(String customerId, String cardId) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", 100);
        chargeParams.put("currency", "EUR");
        chargeParams.put("customer", customerId);
        chargeParams.put("source", cardId);
        Charge charge = Charge.create(chargeParams);

        Map<String, Object> refundParameters = new HashMap<>();
        refundParameters.put("charge", charge.getId());
        Refund refund = refund = Refund.create(refundParameters);
        return refund.getStatus().equalsIgnoreCase("succeeded");
    }

    public Map<String, String> getAllCards(String customerId) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        List<String> paymentSourceId = customer.getSources().getData().stream().map(PaymentSource::getId).collect(Collectors.toList());
        Map<String, String> cardsInfo = new HashMap<>();
        for (String oneSource : paymentSourceId) {
            Card card = (Card) customer.getSources().retrieve(oneSource);
            cardsInfo.put("last4", card.getLast4());
            cardsInfo.put("brand", card.getBrand());
        }
        return cardsInfo;
    }


    public String setDefaultCard(String customerId, String last4) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        List<String> paymentSourceId = customer.getSources().getData().stream().map(PaymentSource::getId).collect(Collectors.toList());
        Map<String, String> cardsInfo = new HashMap<>();
        for (String oneSource : paymentSourceId) {
            Card card = (Card) customer.getSources().retrieve(oneSource);
            if (card.getLast4().equalsIgnoreCase(last4)) {
                customer.setDefaultSource(oneSource);
                return customer.getDefaultSource();
            }
        }
        return null;
    }


    public String deleteCard(String customerId, String last4) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        List<String> paymentSourceId = customer.getSources().getData().stream().map(PaymentSource::getId).collect(Collectors.toList());
        Map<String, String> cardsInfo = new HashMap<>();
        for (String oneSource : paymentSourceId) {
            Card card = (Card) customer.getSources().retrieve(oneSource);
            if (card.getLast4().equalsIgnoreCase(last4)) {
                return card.delete().getLast4();
            }
        }
        return null;
    }
}

