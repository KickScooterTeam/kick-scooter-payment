package com.softserve.paymentservice.configuration;

import com.stripe.Stripe;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Data
@Component
@ConfigurationProperties("stripe")
public class StripeProperties {

    private String publicKey;
    private String privateKey;

    @PostConstruct
    void init() {
        Stripe.apiKey = privateKey;
    }


}
