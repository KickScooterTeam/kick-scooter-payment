package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.model.User;
import com.softserve.paymentservice.model.Invoice;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    User createUser (UUID appUserId);

    Invoice createInvoice(int amount, String customerId);

    boolean addCard(String customerId, CardDto cardDto);

    List<CardDto> getAllCards(String customerId);

    CardDto setDefaultCard(String customerId, String last4);

    CardDto deleteCard(String customerId, String last4);


}
