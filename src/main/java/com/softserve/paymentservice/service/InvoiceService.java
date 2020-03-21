package com.softserve.paymentservice.service;

import com.softserve.paymentservice.model.Invoice;
import com.softserve.paymentservice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public Invoice createInvoice(int amount, String currency, UUID userId) {
        Invoice invoice = new Invoice();
        invoice.setAmount(amount);
        invoice.setCurrency(currency);
        invoice.setDate(new Date());
        invoice.setUserId(userId);
        invoice.setPayed(false);
        invoiceRepository.save(invoice);
        return invoice;
    }

    public Invoice makePayed(UUID userId) {
        Invoice invoice = invoiceRepository.findInvoiceByPayedAndUserId(false, userId);
        invoice.setPayed(true);
        invoiceRepository.save(invoice);
        return invoice;
    }

    public List<Invoice> findInvoices(UUID userId) { //todo add check for two unpaid
        return invoiceRepository.findAllByUserId(userId);
    }

    public List<Invoice> getInvoices(UUID userId) {
        return invoiceRepository.findAllByUserId(userId);
    }

    public Invoice getUnpayed(UUID userId) {
        Invoice invoice = invoiceRepository.findInvoiceByPayedAndUserId(false, userId);
        return invoice;
    }
}
