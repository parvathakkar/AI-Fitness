package com.fitness.userservice.controller;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class userController {


    private UserService userService;

    //healthcheck
    @GetMapping("/health")
    public String healthCheck() {
        System.out.println("Health Endpoint Hit");
        return "OK";
    }

    //singular user
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    //EUREKA ENDPOINT TO VALIDATE USER EXISTENCE
    @GetMapping("/{userId}/validate")
    public ResponseEntity<Boolean> doesUserExist(@PathVariable String userId) {
        return ResponseEntity.ok(userService.doesUserExist(userId));
    }

}
