package com.softserve.paymentservice.service;

import com.softserve.paymentservice.converter.InvoiceToDto;
import com.softserve.paymentservice.dto.InvoiceDto;
import com.softserve.paymentservice.exception.InvoiceNotFoundException;
import com.softserve.paymentservice.exception.UserNotFoundException;
import com.softserve.paymentservice.model.Invoice;
import com.softserve.paymentservice.model.AppUser;
import com.softserve.paymentservice.repository.InvoiceRepository;
import com.softserve.paymentservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final StripePaymentService paymentServiceStripe;

    private final InvoiceToDto invoiceToDto;
    private final KafkaTemplate<String, InvoiceDto> kafkaTemplate;

    public Invoice createInvoice(int amount, UUID userId) throws InvoiceNotFoundException {
        AppUser appUser = userRepository.findAppUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
        Invoice invoice = paymentServiceStripe.createInvoice(amount, appUser.getCustomerId());
        assert invoice != null;
        invoice.setAmount(amount);
        invoice.setUserId(userId);
        invoiceRepository.save(invoice);
        if (invoice.isPaid()) {
            kafkaTemplate.send("email.receipt", invoiceToDto.convert(invoice));
        }
        return invoice;

    }


    public List<Invoice> getInvoices(UUID userId) {
        return invoiceRepository.findAllByUserId(userId);
    }


    public List<Invoice> getUnpaidInvoices(UUID userId) {
        return invoiceRepository.findAllByUserIdAndPaid(userId, false);
    }
}
