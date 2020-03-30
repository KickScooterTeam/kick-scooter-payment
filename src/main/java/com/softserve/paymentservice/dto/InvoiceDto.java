package com.softserve.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {
    private UUID userId;
    private BigDecimal tripCost;
    private String currency;
    private Instant paymentDate;
    private String invoiceId;
}
