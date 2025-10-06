package com.fitness.aiservice.service;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.recRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecService {

    private final recRepo recRepo;
    private final ActivityDeleteService deleteService;

    public List<Recommendation> getUserRecs(String userId) {
        return recRepo.findAllByUserId(userId);
    }

    public Recommendation getActivityRecs(String activityId) {
        return recRepo.findByActivityId(activityId)
                .orElseThrow(()-> new RuntimeException("No Recommendations Found for Activity: " + activityId
                ));
    }

    @Transactional
    public void deleteRecAndActivity(String recId) {
       Recommendation foundRec = recRepo.findById(recId).orElseThrow(()->new RuntimeException("Recommendation for activity does not exist: )"+recId));
        if(foundRec!=null){
            recRepo.deleteById(foundRec.getId());
            deleteService.deleteActivity(foundRec.getActivityId());
            log.info("DELETED FROM RECOMMENDATIONS: {}",foundRec.getId());
            log.info("DELETED FROM ACTIVITIES: {}",foundRec.getActivityId());
        }
    }
}
