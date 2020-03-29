package com.softserve.paymentservice.service;

import lombok.Getter;

@Getter
public enum PricePlan {

    BASIC(10), PREMIUM(1200);

    private int coefficient;

    PricePlan(int coefficient) {
        this.coefficient = coefficient;
    }
}

