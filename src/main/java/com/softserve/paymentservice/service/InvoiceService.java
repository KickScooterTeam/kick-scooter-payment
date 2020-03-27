package com.softserve.paymentservice.service;

import com.softserve.paymentservice.converter.StripeInvoiceToInvoice;
import com.softserve.paymentservice.exception.InvoiceNotFoundException;
import com.softserve.paymentservice.model.Invoice;
import com.softserve.paymentservice.model.User;
import com.softserve.paymentservice.repository.InvoiceRepository;
import com.softserve.paymentservice.repository.UserRepository;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    PaymentService paymentServiceStripe;


    public Invoice createInvoice(int amount, UUID userId) throws InvoiceNotFoundException {
        try {
            User user = userRepository.findUserByUserId(userId).get(); //.orElseThrow(UserNotFoundException::new); //todo how?
            Invoice invoice = new StripeInvoiceToInvoice().convert(paymentServiceStripe.createInvoice(amount, user.getCustomerId()));
            invoice.setAmount(amount);
            invoice.setUserId(userId);
            invoiceRepository.save(invoice);
            return invoice;
        } catch (StripeException e) {
            throw new InvoiceNotFoundException();
        }

    }


    public List<Invoice> getInvoices(UUID userId) {
        return invoiceRepository.findAllByUserId(userId);
    }


    public List<Invoice> getUnpaidInvoices(UUID userId) {
        return invoiceRepository.findAllByUserIdAndPaid(userId, false);
    }
}
