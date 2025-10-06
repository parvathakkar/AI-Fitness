package com.fitness.activityservice.controller;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
public class activityController {

    @Autowired
    ActivityService activityService;

    @GetMapping("activityHealthCheck")
    public String healthcheck(){
        return "Activity is running";
    }

    @PostMapping("/ADDact")
    public ResponseEntity<ActivityResponse> addActivity(@RequestBody ActivityRequest request, @RequestHeader("X-User-ID") String userId){
        if(userId!=null){
            request.setUserId(userId);
        }
        return ResponseEntity.ok(activityService.addActivity(request));
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getActivity(@RequestHeader("X-User-ID") String userId){
        return ResponseEntity.ok(activityService.getActivity(userId));
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable String activityId){
        return ResponseEntity.ok(activityService.getActivityById(activityId));
    }

    @DeleteMapping("/{activityId}")
    public void deleteActivityById(@PathVariable String activityId){
        activityService.deleteActivityById(activityId);
    }


}
