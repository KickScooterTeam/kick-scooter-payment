package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.PaymentInfoDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class AmountCalculator {

    public enum PricePlan {
        BASIC(10), PREMIUM(1200);
        private int coefficient;

        PricePlan(int coefficient) {
            this.coefficient = coefficient;
        }
    }

    public int calculateAmount(PaymentInfoDto paymentInfoDto) {
        BigDecimal amount;
        if (paymentInfoDto.getTariff().equalsIgnoreCase(PricePlan.PREMIUM.toString())) {
            amount = BigDecimal.valueOf(PricePlan.PREMIUM.coefficient);
        } else {
            amount = BigDecimal.valueOf(100).add(BigDecimal.valueOf(PricePlan.BASIC.coefficient).multiply(BigDecimal.valueOf(paymentInfoDto.getMinutes())));
        }

        if (paymentInfoDto.getDiscount() > 0) {
            amount = (amount.multiply(BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(paymentInfoDto.getDiscount())))).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        }

        return amount.intValue();
    }
}
