package com.fitness.aiservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j

public class ActivityDeleteService {

    private final WebClient activityClient;

    public ActivityDeleteService(@Qualifier("activityWebClient") WebClient activityClient) {
        this.activityClient = activityClient;
    }

    public void deleteActivity(String activityId) {
        log.info("Calling Activity Deletion for Activity Id: {}", activityId);
        try{
            activityClient.delete()
                    .uri("/api/activity/{activityId}",activityId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

        } catch (WebClientResponseException e) {
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                throw new RuntimeException("User Not Found for Activity");
            }
            else if(e.getStatusCode().equals(HttpStatus.BAD_REQUEST)){
                throw new RuntimeException("Invalid Request");
            }
        }
    }
}
