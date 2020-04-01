package com.softserve.paymentservice.converter;

import com.softserve.paymentservice.dto.InvoiceDto;
import com.softserve.paymentservice.model.Invoice;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class InvoiceToDto implements Converter<Invoice, InvoiceDto> {
    @Override
    public InvoiceDto convert(Invoice invoice) {
        BigDecimal tripCost = invoice.getAmount().divide(BigDecimal.valueOf(100)).setScale(2);
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setUserId(invoice.getUser().getUserId());
        invoiceDto.setPaymentDate(invoice.getDateCreated());
        invoiceDto.setCurrency(invoice.getCurrency());
        invoiceDto.setTripCost(tripCost);
        invoiceDto.setInvoiceId(invoice.getInvoiceId());
        return invoiceDto;
    }
}


