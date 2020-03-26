package com.softserve.paymentservice.repository;

import com.softserve.paymentservice.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    List<Invoice> findAllByUserId(UUID userId);

    Optional<Invoice> findInvoiceByDateAndUserId(Date date, UUID userId);

    List<Invoice> findInvoicesByPayedAndUserId(Boolean payed, UUID userId);

    Invoice findInvoiceByPayedAndUserId(Boolean payed, UUID userId);


}
