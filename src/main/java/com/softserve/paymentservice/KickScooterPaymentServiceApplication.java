package com.softserve.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class KickScooterPaymentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(KickScooterPaymentServiceApplication.class, args);
    }
}



