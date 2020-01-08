package com.borghol.omcom.basketService.repository;

import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

// import com.borghol.omcom.basketService.model.UserBasket;

import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

@Repository
public class UserBasketRepository {
    
    @Autowired
    DynamoDbAsyncClient dynamoDBAsyncClient;

    @PostConstruct
    public void init() {
        DescribeTableRequest describeTableRequest = DescribeTableRequest.builder().tableName("UserBasket").build();
        CompletableFuture<DescribeTableResponse> response = dynamoDBAsyncClient.describeTable(describeTableRequest);
        Mono.fromCompletionStage(response)
            .onErrorResume((e) -> {
                if (e instanceof ResourceNotFoundException) {
                    CreateTableRequest createTableRequest = CreateTableRequest.builder().

                    return Mono.empty();
                }
                else
                    return Mono.error(e);
            });
        
    }

}