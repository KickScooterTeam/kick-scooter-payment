package com.softserve.paymentservice.repository;

import com.softserve.paymentservice.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    List<Card> findByUserId(UUID userId);

    Optional<Card> findCardByCardNumberAndUserId(String cardNumber, UUID userId);

}
