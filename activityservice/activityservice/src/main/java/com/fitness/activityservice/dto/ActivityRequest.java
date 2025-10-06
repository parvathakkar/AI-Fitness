package com.fitness.activityservice.dto;

import com.fitness.activityservice.model.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityRequest {

    private String userId;
    private Integer duration;
    private Integer calories;
    private ActivityType activityType;
    private LocalDateTime startTime;
    private Map<String, Object> additionalMetrics;
}
