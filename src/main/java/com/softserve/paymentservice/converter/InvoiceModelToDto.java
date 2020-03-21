package com.softserve.paymentservice.converter;

import com.softserve.paymentservice.dto.InvoiceDto;
import com.softserve.paymentservice.model.Invoice;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class InvoiceModelToDto implements Converter<Invoice, InvoiceDto> {
    @Override
    public InvoiceDto convert(Invoice invoice) {
        InvoiceDto invoiceDTO = new InvoiceDto();
        invoiceDTO.setUserUUID(invoice.getUserId());
        invoiceDTO.setAmount(invoice.getAmount());
        invoiceDTO.setCurrency(invoice.getCurrency());
        invoiceDTO.setDate(invoice.getDate());
        invoiceDTO.setPayed(invoice.isPayed());
        return invoiceDTO;
    }
}