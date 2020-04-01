package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.exception.CardNotFoundException;
import com.softserve.paymentservice.exception.CardParametersException;
import com.softserve.paymentservice.exception.InvoiceNotFoundException;
import com.softserve.paymentservice.exception.UserCreationException;
import com.softserve.paymentservice.model.Invoice;
import com.softserve.paymentservice.model.User;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripePaymentService implements PaymentService {

    private final ConversionService conversionService;


    @Override
    public User createUser(UUID userId) {
        try {
            Map<String, Object> customerParameter = new HashMap<>();
            customerParameter.put("name", userId);
            User user = new User();
            user.setUserId(userId);
            user.setCustomerId(Customer.create(customerParameter).getId());
            return user;
        } catch (StripeException stripeException) {
            throw new UserCreationException(stripeException.toString());
        }
    }

    @Override
    public Invoice createInvoice(int amount, User user) {
        try {
            Map<String, Object> invoiceItemParams = new HashMap<>();
            invoiceItemParams.put("customer", user.getCustomerId());
            invoiceItemParams.put("amount", amount);
            invoiceItemParams.put("currency", "EUR");
            invoiceItemParams.put("description", "date  " + Instant.now());
            InvoiceItem.create(invoiceItemParams);

            Map<String, Object> invoiceParams = new HashMap<>();
            invoiceParams.put("customer", user.getCustomerId());
            invoiceParams.put("auto_advance", false);
            invoiceParams.put("collection_method", "charge_automatically");
            var invoiceStripe = com.stripe.model.Invoice.create(invoiceParams);
            var paidInvoiceStripe = invoiceStripe.pay();


            return conversionService.convert(paidInvoiceStripe, Invoice.class);
        } catch (StripeException stripeException) {
            throw new InvoiceNotFoundException(stripeException.toString());
        }
    }


    @Override
    public Invoice payUnpaidInvoice(String invoiceId) {
        try {
            var invoiceStripe = com.stripe.model.Invoice.retrieve(invoiceId).pay();
            if (invoiceStripe.getPaid()) {
                return conversionService.convert(invoiceStripe, Invoice.class);
            } else {
                throw new InvoiceNotFoundException("Stripe connection problem");
            }
        } catch (StripeException stripeException) {
            throw new InvoiceNotFoundException(stripeException.toString());
        }
    }

    @Override
    public boolean addCard(User user, CardDto cardDto) {
        try {
            Customer customer = Customer.retrieve(user.getCustomerId());
            Map<String, Object> cardParameters = new HashMap<>();

            cardParameters.put("number", cardDto.getCardNumber());
            cardParameters.put("exp_month", cardDto.getYearMonth().getMonthValue());
            cardParameters.put("exp_year", cardDto.getYearMonth().getYear());
            cardParameters.put("cvc", cardDto.getCvc());

            Map<String, Object> tokenParameters = new HashMap<>();
            tokenParameters.put("card", cardParameters);
            Token token = Token.create(tokenParameters);
            log.info("token : {}", token);

            Map<String, Object> source = new HashMap<>();
            source.put("source", token.getId());
            return checkCard(user, customer.getSources().create(source).getId());

        } catch (StripeException stripeException) {
            throw new CardParametersException(stripeException.toString());
        }
    }

    private boolean checkCard(User user, String cardId) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", 100);
        chargeParams.put("currency", "EUR");
        chargeParams.put("customer", user.getCustomerId());
        chargeParams.put("source", cardId);
        Charge charge = Charge.create(chargeParams);

        Map<String, Object> refundParameters = new HashMap<>();
        refundParameters.put("charge", charge.getId());
        Refund refund = Refund.create(refundParameters);
        return refund.getStatus().equalsIgnoreCase("succeeded");
    }

    @Override
    public List<CardDto> getAllCards(User user) {
        try {
            Customer customer = Customer.retrieve(user.getCustomerId());
            List<String> paymentSourceId = customer.getSources().getData().stream().map(PaymentSource::getId).collect(Collectors.toList());
            List<CardDto> cardsInfo = new ArrayList<>();
            for (String oneSource : paymentSourceId) {
                Card card = (Card) customer.getSources().retrieve(oneSource);
                cardsInfo.add(new CardDto(Integer.parseInt(card.getLast4()), card.getBrand()));
            }
            return cardsInfo;
        } catch (StripeException stripeException) {
            throw new CardNotFoundException(stripeException.toString());
        }
    }

    @Override
    public CardDto setDefaultCard(User user, String last4) {
        try {
            Customer customer = Customer.retrieve(user.getCustomerId());
            List<String> paymentSourceId = customer.getSources().getData().stream().map(PaymentSource::getId)
                    .collect(Collectors.toList());
            for (String oneSource : paymentSourceId) {
                Card card = (Card) customer.getSources().retrieve(oneSource);
                if (card.getLast4().equals(last4)) {
                    Map<String,Object> customerParams = new HashMap<>();
                    customerParams.put("default_source", card.getId());
                    customer.update(customerParams);
                    return new CardDto(Integer.parseInt(card.getLast4()), card.getBrand());
                }
            }
            return null;
        } catch (StripeException stripeException) {
            throw new CardNotFoundException(stripeException.toString());
        }
    }

    @Override
    public CardDto deleteCard(User user, String last4) {
        try {
            Customer customer = Customer.retrieve(user.getCustomerId());
            List<String> paymentSourceId = customer.getSources().getData().stream().map(PaymentSource::getId)
                    .collect(Collectors.toList());
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

