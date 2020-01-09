package com.borghol.omcom.basketService.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Configuration
@EnableWebFlux
public class DynamoDBConfig {
    
    @Value("${dynamo.endpoint}")
    private String amazonDynamoDBEndpoint;
    

    @Bean
    public DynamoDbAsyncClient dynamoDBAsyncClient() {
        return DynamoDbAsyncClient.builder()
                .endpointOverride(URI.create(amazonDynamoDBEndpoint))
                .region(Region.EU_WEST_1)
                .build();
    }
}