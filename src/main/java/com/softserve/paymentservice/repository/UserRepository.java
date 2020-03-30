package com.softserve.paymentservice.repository;

import com.softserve.paymentservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {


}
