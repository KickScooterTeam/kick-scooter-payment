package com.softserve.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String invoiceId;

    private Instant dateCreated;

    private BigDecimal amount;
    private boolean paid;
    private String currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    User user;

}