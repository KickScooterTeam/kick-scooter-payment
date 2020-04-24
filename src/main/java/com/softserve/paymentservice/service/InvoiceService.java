package com.softserve.paymentservice.service;

import com.softserve.paymentservice.converter.InvoiceToDto;
import com.softserve.paymentservice.dto.InvoiceDto;
import com.softserve.paymentservice.exception.InvoiceNotFoundException;
import com.softserve.paymentservice.model.Invoice;
import com.softserve.paymentservice.model.User;
import com.softserve.paymentservice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {
    private final PaymentService paymentService;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceToDto invoiceToDto;
    private final KafkaTemplate<String, InvoiceDto> kafkaTemplate;

    public InvoiceDto createInvoice(BigDecimal amount, User user) {
        Invoice invoice = paymentService.createInvoice(amount.intValue(), user);
        invoice.setAmount(amount);
        invoice.setUser(user);
        invoiceRepository.save(invoice);
        sentToKafka(invoice);
        return invoiceToDto.convert(invoice);
    }


    public List<InvoiceDto> getInvoices(User user) {
        return invoiceRepository.findAllByUser(user).stream().map(invoiceToDto::convert)
                .collect(Collectors.toList());
    }

    public boolean hasUnpaidInvoice(User user) {
        return invoiceRepository.findByUserAndPaid(user, false).isPresent();
    }


    public boolean payUnpaidInvoice(User user) {
        Invoice invoice = paymentService.payUnpaidInvoice(
                invoiceRepository.findByUserAndPaid(user, false)
                        .orElseThrow(() -> new InvoiceNotFoundException("All invoices are paid")).getInvoiceId());
        invoiceRepository.save(invoice);
        sentToKafka(invoice);
        return invoice.isPaid();
    }


    private void sentToKafka(Invoice invoice) {
        if (invoice.isPaid()) {
            kafkaTemplate.send("email-receipt", invoiceToDto.convert(invoice));
            log.info("receipt sent");
        }
    }
}
