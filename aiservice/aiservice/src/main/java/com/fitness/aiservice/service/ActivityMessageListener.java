package com.fitness.aiservice.service;
import com.fitness.aiservice.repository.recRepo;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final ActivityAIService activityAIService;
    private final recRepo recRepo;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.queue.name}")
    private static String queue;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;


    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity){
        log.info("Received Activity for processing: {}", activity.getId());
        Recommendation recommendation = activityAIService.generateRecommendation(activity);
        recRepo.save(recommendation);
    }
}
