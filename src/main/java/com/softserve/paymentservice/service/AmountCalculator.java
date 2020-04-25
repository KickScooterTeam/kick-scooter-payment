package com.softserve.paymentservice.service;

import com.softserve.paymentservice.dto.PaymentInfoDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AmountCalculator {

    public BigDecimal calculateAmount(PaymentInfoDto paymentInfoDto) {
        return BigDecimal.valueOf(100).add(BigDecimal.valueOf(PricePlan.BASIC.getCoefficient())
                .multiply(BigDecimal.valueOf(paymentInfoDto.getTripTime().toMinutes())));
    }


}
