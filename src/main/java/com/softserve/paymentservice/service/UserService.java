package com.softserve.paymentservice.service;

import com.softserve.paymentservice.exception.UserNotFoundException;
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


    public User createUser(UUID userId) {
        return userRepository.save(paymentService.createUser(userId));
    }

    public User getOrCreateUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseGet(() -> createUser(userId));
    }
    public boolean isUserCreated(UUID userId){
        return userRepository.existsById(userId);
    }
    public User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
    }
}
