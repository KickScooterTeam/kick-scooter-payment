package com.softserve.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoDto {
    private int minutes;
    private int discount;
    private String tariff;
    private UUID userid;
    private String currency;
}
