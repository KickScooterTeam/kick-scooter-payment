package com.softserve.paymentservice.converter;

import com.softserve.paymentservice.dto.InvoiceDto;
import com.softserve.paymentservice.model.Invoice;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class InvoiceToDto implements Converter<Invoice, InvoiceDto> {
    @Override
    public InvoiceDto convert(Invoice invoice) {
        BigDecimal tripCost = BigDecimal.valueOf(invoice.getAmount()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP); //todo test
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setUserId(invoice.getAppUser().getUserId());
        invoiceDto.setPaymentDate(invoice.getDateCreated());
        invoiceDto.setCurrency(invoice.getCurrency());
        invoiceDto.setTripCost(tripCost);
        return invoiceDto;
    }


}


