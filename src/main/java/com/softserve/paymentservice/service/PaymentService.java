package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.exception.CardNotFoundException;
import com.softserve.paymentservice.exception.CardParametersException;
import com.softserve.paymentservice.exception.InvoiceNotFoundException;
import com.softserve.paymentservice.exception.UserCreationException;
import com.softserve.paymentservice.model.Invoice;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    String createCustomer(UUID userId) throws UserCreationException;

    Invoice createInvoice(int amount, String customerId) throws InvoiceNotFoundException;

    boolean addCard(String customerId, CardDto cardDto) throws CardParametersException;

    List<CardDto> getAllCards(String customerId) throws CardNotFoundException;

    CardDto setDefaultCard(String customerId, String last4) throws CardNotFoundException;

    CardDto deleteCard(String customerId, String last4) throws CardNotFoundException;


}
