package com.fitness.aiservice.controller;

import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.service.RecService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recs")
@RequiredArgsConstructor
public class recController {


    private final RecService recService;

    @GetMapping("/health")
    public String aiHealthCheck(){
        return "Ai is running";
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getUserRecs(@PathVariable String userId){
        return ResponseEntity.ok(recService.getUserRecs(userId));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getActivityRecs(@PathVariable String activityId){
        return ResponseEntity.ok(recService.getActivityRecs(activityId));
    }

    @DeleteMapping("/delete/{recId}")
    public void deleteRecAndActivity(@PathVariable String recId){
        recService.deleteRecAndActivity(recId);
    }

}
