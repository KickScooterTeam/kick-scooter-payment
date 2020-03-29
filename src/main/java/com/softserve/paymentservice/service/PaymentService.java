package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.model.Invoice;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    String createCustomer(UUID userId);

    Invoice createInvoice(int amount, String customerId);

    boolean addCard(String customerId, CardDto cardDto);

    List<CardDto> getAllCards(String customerId);

    CardDto setDefaultCard(String customerId, String last4);

    CardDto deleteCard(String customerId, String last4);


}
