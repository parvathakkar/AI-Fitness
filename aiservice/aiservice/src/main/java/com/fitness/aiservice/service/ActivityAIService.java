package com.fitness.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("Repose from AI is {}", aiResponse);
        return processAiResponse(activity, aiResponse); //does not do anything as of rn

    }

    private Recommendation processAiResponse(Activity activity, String response){
        try{

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            JsonNode textNode = rootNode.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text");
            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n","")
                    .replaceAll("\\n```","")
                    .trim();

            JsonNode analysisJson = mapper.readTree(jsonContent);
            JsonNode analysis = analysisJson.path("analysis");

            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis,analysis,"overall", "Overall: ");
            addAnalysisSection(fullAnalysis,analysis,"pace", "Pace: ");
            addAnalysisSection(fullAnalysis,analysis,"heartRate", "Heart Rate: ");
            addAnalysisSection(fullAnalysis,analysis,"caloriesBurned", "Calories: ");



            List<String> ListOfimprovements = extractImprovements(analysisJson.path("improvements"));


            List<String> ListOfSuggestions = extractSuggestions(analysisJson.path("suggestions"));


            List<String> ListOfSafetyNotes = extractSafetyNotes(analysisJson.path("safety"));

            log.info("Cleaned up Json content for response is {}", jsonContent);
            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .rec(fullAnalysis.toString().trim())
                    .improvements(ListOfimprovements)
                    .suggestions(ListOfSuggestions)
                    .safety(ListOfSafetyNotes)
                    .createdAt(LocalDateTime.now())
                    .build();



        } catch (Exception e) {
            log.error("Something Went Wrong");
            return createDefaultRecommendation(activity);
        }
    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType())
                .rec("unable to generate recs")
                .improvements(Collections.singletonList("Continue with your current exercise"))
                .suggestions(Collections.singletonList("Stretch"))
                .safety(Collections.singletonList("Stay safe"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSafetyNotes(JsonNode safetyNode) {
        List<String> sft = new ArrayList<>();
        if(safetyNode.isArray()){
            log.info("SAFETY IS ARRAY");
            safetyNode.forEach(safety -> {
                sft.add(safety.asText());
            });
        }
        return sft.isEmpty()
                ? Collections.singletonList("No Safety Notes Found")
                : sft;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> sug = new ArrayList<>();
        if(suggestionsNode.isArray()){
            log.info("SUGGESTIONS IS ARRAY");
            suggestionsNode.forEach(suggestion -> {
                String wkt = suggestion.path("workout").asText();
                String des = suggestion.path("description").asText();
                sug.add(String.format("%s: %s", wkt, des));
            });
        }
        return sug.isEmpty()
                ? Collections.singletonList("No Suggestions Found")
                : sug;
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> imps = new ArrayList<>();
        if(improvementsNode.isArray()){
            log.info("IMPROVEMENTS IS ARRAY");
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String rec = improvement.path("recommendation").asText();
                imps.add(String.format("%s: %s", area, rec));
            });
        }
        return imps.isEmpty()
                ? Collections.singletonList("No improvements Found")
                : imps;
    }

    private void addAnalysisSection(StringBuilder builder, JsonNode analysis, String key, String prefix) {
        if(!analysis.path(key).isMissingNode()){
            builder.append(prefix)
                    .append(analysis.path(key).asText())
                    .append("\n\n");
        }

    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
        Analyze this fitness activity and provide detailed recommendations in following EXACT JSON format:
        {
            "analysis: {
                "overall": "Overall analysis here",
                "pace": "pace analysis here",
                "heartRate": "Heart Rate analysis here",
                "caloriesBurned": "Calories analysis here"
            },
            "improvements": [
               {
                   "area": "Area Name",
                   "recommendation": "detailed recommendation"
               }
            ],
            "suggestions": [
                {
                   "workout": "workout name",
                   "description": "detailed workout Description"
                }
            ],
            "safety": [
                "Safety point 1",
                "Safety point 2"
            ]
        }
       \s
        Analyze this activity:
        Activity Type: %s,
        Duration: %d minutes,
        Calories burned: %d
        Additional Metrics %s
       \s
        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
        Ensure the response follows the EXACT JSON format shown above.
       \s""",
                    activity.getType(),
                    activity.getDuration(),
                    activity.getCaloriesBurned(),
                    activity.getAdditionalMetrics()
            );
    }
}
