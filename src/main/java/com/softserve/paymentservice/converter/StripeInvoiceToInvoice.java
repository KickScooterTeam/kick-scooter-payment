package com.softserve.paymentservice.converter;

import com.softserve.paymentservice.model.Invoice;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class StripeInvoiceToInvoice implements Converter<com.stripe.model.Invoice, Invoice> {
    @Override
    public Invoice convert(com.stripe.model.Invoice invoiceStripe) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceStripe.getId());
        invoice.setDateCreated(Instant.now());
        invoice.setPaid(invoiceStripe.getPaid()); //todo wait the response from Stripe ||    invoice.setPaid(true);  //
        invoice.setCurrency(invoiceStripe.getCurrency());
        return invoice;
    }
}
