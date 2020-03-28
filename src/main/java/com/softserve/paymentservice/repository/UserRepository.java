package com.softserve.paymentservice.repository;

import com.softserve.paymentservice.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {

}