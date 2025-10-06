package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    //inject the repository which will be used for querying db
    @Autowired
    private UserRepository repo;

    // adding an entry into the users table
    public UserResponse register(RegisterRequest request) {
        User user = new User();

        if (repo.existsByEmail(request.getEmail())) {
            User existingUser = repo.findByEmail(request.getEmail());
            return getUserResponse(existingUser);
        }

        user.setEmail(request.getEmail());
        user.setKeyCloakId(request.getKeyCloakId());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User savedUser = repo.save(user);

        return getUserResponse(savedUser);
    }

    private UserResponse getUserResponse(User savedUser) {
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setId(savedUser.getId());
        userResponse.setKeyCloakId(savedUser.getKeyCloakId());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());
        userResponse.setCreatedAt(savedUser.getCreatedAt());

        return userResponse;
    }

    public UserResponse getUserProfile(String userId) {
        User foundUser = repo.findById(userId).orElseThrow(()-> new RuntimeException("Not found"));
        return getUserResponse(foundUser);
    }

    public Boolean doesUserExist(String userId) {
        log.info("Calling User Validation API for userId: {}", userId);
        return repo.existsByKeyCloakId(userId);
    }
}