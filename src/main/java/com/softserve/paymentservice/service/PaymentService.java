package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.model.Invoice;
import com.softserve.paymentservice.model.User;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    User createUser(UUID appUserId);

    Invoice createInvoice(int amount, User user);

    Invoice payUnpaidInvoice(String invoiceId);

    boolean addCard(User user, CardDto cardDto);

    List<CardDto> getAllCards(User user);

    CardDto setDefaultCard(User user, String last4);

    CardDto deleteCard(User user, String last4);

}
