package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.PaymentInfoDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class AmountCalculator {

    public BigDecimal calculateAmount(PaymentInfoDto paymentInfoDto) {

        BigDecimal amount = BigDecimal.valueOf(100).add(BigDecimal.valueOf(PricePlan.BASIC.getCoefficient())
                .multiply(BigDecimal.valueOf(paymentInfoDto.getDuration().toMinutes())));

        if (paymentInfoDto.getDiscount() > 0) {
            amount = (amount.multiply(BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(paymentInfoDto.getDiscount()))))
                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        }
        return amount;
    }


}
