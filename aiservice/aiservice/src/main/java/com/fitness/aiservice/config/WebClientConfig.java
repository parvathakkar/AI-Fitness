package com.fitness.aiservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

// aiservice/config/WebClientConfig.java
@Configuration
public class WebClientConfig {

    @Bean @LoadBalanced
    public WebClient.Builder lbBuilder() { return WebClient.builder(); }   // for Eureka

    @Bean
    public WebClient.Builder plainBuilder() { return WebClient.builder(); } // for internet

    @Bean(name = "activityWebClient")
    public WebClient activityWebClient(@Qualifier("lbBuilder") WebClient.Builder b) {
        return b.baseUrl("lb://activityservice").build(); // internal via Eureka
    }

    @Bean(name = "geminiWebClient")
    public WebClient geminiWebClient(@Qualifier("plainBuilder") WebClient.Builder b) {
        return b.baseUrl("https://generativelanguage.googleapis.com").build(); // external
    }
}

