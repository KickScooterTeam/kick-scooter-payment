package com.softserve.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoDto {
    private Duration duration;
    private int discount;
    private String tariff;
    private UUID userId;
}

