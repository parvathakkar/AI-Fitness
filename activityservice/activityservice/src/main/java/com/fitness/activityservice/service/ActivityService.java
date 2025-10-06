package com.fitness.activityservice.service;

import com.fitness.activityservice.config.RabbitMqConfig;
import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {


    private final ActivityRepository repo;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse addActivity(ActivityRequest request){

       //validate existence of id before we can perform the add
        System.out.println(userValidationService.validateUser(request.getUserId()));
        if(userValidationService.validateUser(request.getUserId())){
            Activity activity = Activity.builder()
                    .userId(request.getUserId())
                    .type(request.getActivityType())
                    .caloriesBurned(request.getCalories())
                    .duration(request.getDuration())
                    .additionalMetrics(request.getAdditionalMetrics())
                    .startTime(request.getStartTime())
                    .build();

            Activity savedActivity = repo.save(activity);

            //publish into the rabbitmq queue
            try{
                rabbitTemplate.convertAndSend(exchange,routingKey,savedActivity);
                log.info("published into queue");
            }catch (Exception e){
                log.error("Failed to Publish activity into Queue: {}", String.valueOf(e));
            }

            return activityToResponse(savedActivity);
        }

        throw new RuntimeException("Invalid User Request: "+ request.getUserId());
    }

    private ActivityResponse activityToResponse(Activity savedActivity){
        return ActivityResponse.builder()
                .id(savedActivity.getId())
                .userId(savedActivity.getUserId())
                .type(savedActivity.getType())
                .duration(savedActivity.getDuration())
                .caloriesBurned(savedActivity.getCaloriesBurned())
                .startTime(savedActivity.getStartTime())
                .additionalMetrics(savedActivity.getAdditionalMetrics())
                .createdAt(savedActivity.getCreatedAt())
                .updatedAt(savedActivity.getUpdatedAt())
                .build();
    }

    public List<ActivityResponse> getActivity(String userId) {
        List<Activity> foundActivityForUser = repo.findByUserId(userId);
        return foundActivityForUser.stream().map(this::activityToResponse).collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(String activityId) {
        Activity foundActivityById = repo.findById(activityId).orElseThrow(()-> new RuntimeException("Activity Not Found"));
        return activityToResponse(foundActivityById);
    }

    public void deleteActivityById(String activityId) {
        Activity foundActivityById = repo.findById(activityId).orElseThrow(()-> new RuntimeException("Activity Not Found"));
        if(foundActivityById!=null){
            repo.deleteById(activityId);
        }
    }
}

