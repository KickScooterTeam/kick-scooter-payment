package com.softserve.paymentservice.kafka;

import com.softserve.paymentservice.dto.PaymentInfoDto;
import com.softserve.paymentservice.service.AmountCalculator;
import com.softserve.paymentservice.service.InvoiceService;
import com.softserve.paymentservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final AmountCalculator amountCalculator;
    private final InvoiceService invoiceService;
    private final UserService userService;

    @KafkaListener(topics = "payment-info")
    public void listenPaymentInfo(PaymentInfoDto paymentInfoDto) {
        log.info("Received payment info for user");
        invoiceService.createInvoice(amountCalculator.calculateAmount(paymentInfoDto),
                userService.getUser(paymentInfoDto.getUserId()));
    }
}
