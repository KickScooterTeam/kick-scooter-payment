package com.softserve.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    private UUID userUUID;
    private String cardNumber;
//    private YearMonth ym; //todo how to set it?.
    private int year;
    private int month;
    private int cvc;
    private int last4;
    private String brand;

    public CardDto(int last4, String brand) {
        this.last4 = last4;
        this.brand = brand;
    }
}
