package com.softserve.paymentservice.service;

import com.softserve.paymentservice.model.User;
import com.softserve.paymentservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PaymentService paymentService;

    private User createUser(UUID userId){
        User user = userRepository.findById(userId)
                .orElseGet(()->userRepository.save(new User(userId, paymentService.createUser(userId)))); //todo in UserService createUser / findUser getOrCreate method --> for

        return user;
    }





}
