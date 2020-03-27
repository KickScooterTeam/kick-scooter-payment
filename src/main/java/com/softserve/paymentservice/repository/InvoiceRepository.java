package com.softserve.paymentservice.repository;

import com.softserve.paymentservice.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    List<Invoice> findAllByUserId(UUID userId);

    List<Invoice> findAllByUserIdAndPaid(UUID userId, boolean paid);

    Optional<Invoice> findByUserIdAndPaid(UUID userId, boolean paid);

}
