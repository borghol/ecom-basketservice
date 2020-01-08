package com.borghol.omcom.basketService.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.config.EnableWebFlux;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Configuration
@EnableWebFlux
public class DynamoDBConfig {
    
    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.dynamodb.region}")
    private String amazonDynamoDBRegion;

    @Value("${amazon.aws.accesskey}")
	private String amazonAWSAccessKey;

	@Value("${amazon.aws.secretkey}")
	private String amazonAWSSecretKey;

    private AwsCredentialsProvider amazonAWSCredentialsProvider() {

        return StaticCredentialsProvider.create(amazonAWSCredentials());
    }

    private AwsCredentials amazonAWSCredentials() {
        
    }

    @Bean
    public DynamoDbAsyncClient dynamoDBAsyncClient() {
        return DynamoDbAsyncClient.builder()
                .region(Region.EU_WEST_1)
                .endpointOverride(URI.create(amazonDynamoDBEndpoint))
                .credentialsProvider(amazonAWSCredentialsProvider())
                .build();
    }
}