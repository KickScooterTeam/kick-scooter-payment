package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.exception.CardNotFoundException;
import com.softserve.paymentservice.exception.CardParametersException;
import com.softserve.paymentservice.exception.InvoiceNotFoundException;
import com.softserve.paymentservice.exception.UserCreationException;
import com.softserve.paymentservice.model.Invoice;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StripePaymentService implements PaymentService {

    final ConversionService conversionService;

    @Value("${stripe.key.secret}")
    private String stripeSecretKey;

    @Override
    public String createCustomer(UUID userId) throws UserCreationException {
        try {
            Map<String, Object> customerParameter = new HashMap<>();
            customerParameter.put("name", userId);
            return Customer.create(customerParameter).getId();
        } catch (StripeException stripeException) {
            throw new UserCreationException(stripeException.toString());
        }
    }

    @Override
    public Invoice createInvoice(int amount, String customerId) throws InvoiceNotFoundException {
        try {
            Map<String, Object> invoiceItemParams = new HashMap<>();
            invoiceItemParams.put("customer", customerId);
            invoiceItemParams.put("amount", amount);
            invoiceItemParams.put("currency", "EUR");
            invoiceItemParams.put("description", "date  " + Instant.now());
            InvoiceItem.create(invoiceItemParams);

            Map<String, Object> invoiceParams = new HashMap<>();
            invoiceParams.put("customer", customerId);
            invoiceParams.put("auto_advance", false);
            invoiceParams.put("collection_method", "charge_automatically");
            com.stripe.model.Invoice invoiceStripe = com.stripe.model.Invoice.create(invoiceParams);
            invoiceStripe.pay();

            return conversionService.convert(invoiceStripe, Invoice.class);
        } catch (StripeException stripeException) {
            throw new InvoiceNotFoundException(stripeException.toString());
        }
    }

    @Override
    public boolean addCard(String customerId, CardDto cardDto) throws CardParametersException {
        try {
            Customer customer = Customer.retrieve(customerId);
            Map<String, Object> cardParameters = new HashMap<>();

            cardParameters.put("number", cardDto.getCardNumber());
            cardParameters.put("exp_month", cardDto.getMonth());
            cardParameters.put("exp_year", cardDto.getYear());
            cardParameters.put("cvc", cardDto.getCvc());

            Map<String, Object> tokenParameters = new HashMap<>();
            tokenParameters.put("card", cardParameters);
            Token token = Token.create(tokenParameters);

            Map<String, Object> source = new HashMap<>();
            source.put("source", token.getId());
            return checkCard(customerId, customer.getSources().create(source).getId());

        } catch (StripeException stripeException) {
            throw new CardParametersException(stripeException.toString());
        } catch (InvoiceNotFoundException invoiceNotFound) {
            throw new CardParametersException(invoiceNotFound.getMessage());
        }
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
        Refund refund = Refund.create(refundParameters);
        return refund.getStatus().equalsIgnoreCase("succeeded");
    }

    @Override
    public List<CardDto> getAllCards(String customerId) throws CardNotFoundException {
        try {
            Customer customer = Customer.retrieve(customerId);
            List<String> paymentSourceId = customer.getSources().getData().stream().map(PaymentSource::getId).collect(Collectors.toList());
            List<CardDto> cardsInfo = new ArrayList<>();
            for (String oneSource : paymentSourceId) {
                Card card = (Card) customer.getSources().retrieve(oneSource);
                cardsInfo.add(new CardDto(Integer.parseInt(card.getLast4()), card.getBrand()));
            }
            return cardsInfo;
        } catch (StripeException stripeException) {
            throw new CardNotFoundException(stripeException.getMessage());
        }
    }

    @Override
    public CardDto setDefaultCard(String customerId, String last4) throws CardNotFoundException {
        try {
            Customer customer = Customer.retrieve(customerId);
            List<String> paymentSourceId = customer.getSources().getData().stream().map(PaymentSource::getId).collect(Collectors.toList());
            for (String oneSource : paymentSourceId) {
                Card card = (Card) customer.getSources().retrieve(oneSource);
                if (card.getLast4().equals(last4)) {
                    customer.setDefaultSource(oneSource);
                    return new CardDto(Integer.parseInt(card.getLast4()), card.getBrand());
                }
            }
            return null;
        } catch (StripeException stripeException) {
            throw new CardNotFoundException(stripeException.toString());
        }
    }

    @Override
    public CardDto deleteCard(String customerId, String last4) throws CardNotFoundException {
        try {
            Customer customer = Customer.retrieve(customerId);
            List<String> paymentSourceId = customer.getSources().getData().stream().map(PaymentSource::getId).collect(Collectors.toList());
            for (String oneSource : paymentSourceId) {
                Card card = (Card) customer.getSources().retrieve(oneSource);
                if (card.getLast4().equals(last4)) {
                    card.delete();
                    return new CardDto(Integer.parseInt(card.getLast4()), card.getBrand());
                }
            }
            return null;
        } catch (StripeException stripeException) {
            throw new CardNotFoundException(stripeException.toString());
        }
    }

}

