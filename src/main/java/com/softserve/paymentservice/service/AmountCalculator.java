package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.PaymentInfoDto;
import org.springframework.stereotype.Service;

@Service
public class AmountCalculator {

    private int amount;

    public enum Tariffs {
        BASIC(10), PREMIUM(1200);
        private int coefficient;

        Tariffs(int coefficient) {
            this.coefficient = coefficient;
        }
    }

    public int calculateAmount(PaymentInfoDto paymentInfoDto) {
        if (paymentInfoDto.getTariff().equalsIgnoreCase(Tariffs.PREMIUM.toString())) {  //Tariffs.valueOf(tariff).toString().equals("PREMIUM")
            amount = Tariffs.PREMIUM.coefficient;
        } else {
            if (paymentInfoDto.getMinutes() > 0) {
                amount =100+ Tariffs.valueOf(paymentInfoDto.getTariff()).coefficient * paymentInfoDto.getMinutes();
            }
            if (paymentInfoDto.getDiscount() > 0) {
                amount = (amount * (100 - paymentInfoDto.getDiscount())) / 100;
            }
        }
        return amount;
    }
}
