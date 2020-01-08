package com.borghol.omcom.basketService.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

// import com.borghol.omcom.basketService.model.UserBasket;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

@Repository
public class UserBasketRepository {
    
    @Autowired
    DynamoDbAsyncClient dynamoDBAsyncClient;

    @PostConstruct
    public void init() {
        DescribeTableRequest describeTableRequest = DescribeTableRequest.builder().tableName("UserBasket").build();
        CompletableFuture<DescribeTableResponse> response = dynamoDBAsyncClient.describeTable(describeTableRequest);
        Mono.fromFuture(response)
            .map(x -> {
                HashMap<String,AttributeValue> item_values =
                    new HashMap<String,AttributeValue>();
                item_values.put("userId", AttributeValue.builder().s("borghol").build());
                item_values.put("itemId", AttributeValue.builder().s("someItem").build());
                item_values.put("count", AttributeValue.builder().n("1").build());
                PutItemRequest request = PutItemRequest.builder()
		            .tableName("UserBasket")
		            .item(item_values)
                    .build();

                return Mono.fromFuture(dynamoDBAsyncClient.putItem(request));
            })
            .onErrorResume((e) -> {
                if (e instanceof ResourceNotFoundException) {
                    List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
                    attributeDefinitions.add(AttributeDefinition.builder().attributeName("userId").attributeType("S").build());
                    attributeDefinitions.add(AttributeDefinition.builder().attributeName("itemId").attributeType("S").build());

                    List<KeySchemaElement> keySchemaElements = new ArrayList<>();
                    keySchemaElements.add(KeySchemaElement.builder().attributeName("userId").keyType(KeyType.HASH).build());
                    keySchemaElements.add(KeySchemaElement.builder().attributeName("itemId").keyType(KeyType.RANGE).build());

                    CreateTableRequest createTableRequest = CreateTableRequest.builder()
                                                    .tableName("UserBasket")
                                                    .attributeDefinitions(attributeDefinitions)
                                                    .keySchema(keySchemaElements)
                                                    .provisionedThroughput(ProvisionedThroughput.builder().writeCapacityUnits(5L).readCapacityUnits(9L).build())
                                                    .build();
                    Mono.fromFuture(dynamoDBAsyncClient.createTable(createTableRequest)).subscribe();
                    return Mono.empty();
                }
                else
                    return Mono.error(e);
            }).block();
    }

}