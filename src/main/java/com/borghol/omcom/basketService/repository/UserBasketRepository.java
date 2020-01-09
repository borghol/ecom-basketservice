package com.borghol.omcom.basketService.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import com.borghol.omcom.basketService.model.UserBasketItem;
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
import software.amazon.awssdk.services.dynamodb.model.GlobalSecondaryIndex;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

@Repository
public class UserBasketRepository {

    @Autowired
    DynamoDbAsyncClient dynamoDBAsyncClient;

    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        final DescribeTableRequest describeTableRequest = DescribeTableRequest.builder().tableName("UserBasket").build();
        final CompletableFuture<DescribeTableResponse> response = dynamoDBAsyncClient.describeTable(describeTableRequest);
        Mono.fromFuture(response)
            .map(x -> {
                final HashMap<String,AttributeValue> item_values =
                    new HashMap<String,AttributeValue>();
                item_values.put("userId", AttributeValue.builder().s("borghol").build());
                item_values.put("itemId", AttributeValue.builder().s("someItem").build());
                item_values.put("count", AttributeValue.builder().n("1").build());
                final PutItemRequest request = PutItemRequest.builder()
		            .tableName("UserBasket")
		            .item(item_values)
                    .build();

                return Mono.fromFuture(dynamoDBAsyncClient.putItem(request));
            })
            .onErrorResume((e) -> {
                if (e instanceof ResourceNotFoundException) {
                    final List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
                    attributeDefinitions.add(AttributeDefinition.builder().attributeName("userId").attributeType("S").build());
                    attributeDefinitions.add(AttributeDefinition.builder().attributeName("itemId").attributeType("S").build());
                    attributeDefinitions.add(AttributeDefinition.builder().attributeName("dateUpdated").attributeType("S").build());
                    attributeDefinitions.add(AttributeDefinition.builder().attributeName("dateUpdated_ItemId").attributeType("S").build());

                    final List<KeySchemaElement> primaryElements = new ArrayList<>();
                    primaryElements.add(KeySchemaElement.builder().attributeName("userId").keyType(KeyType.HASH).build());
                    primaryElements.add(KeySchemaElement.builder().attributeName("itemId").keyType(KeyType.RANGE).build());

                    final List<KeySchemaElement> gsi1Keys = new ArrayList<>();
                    gsi1Keys.add(KeySchemaElement.builder().attributeName("itemId").keyType(KeyType.HASH).build());
                    gsi1Keys.add(KeySchemaElement.builder().attributeName("userId").keyType(KeyType.RANGE).build());

                    final List<KeySchemaElement> gsi2Keys = new ArrayList<>();
                    gsi2Keys.add(KeySchemaElement.builder().attributeName("userId").keyType(KeyType.HASH).build());
                    gsi2Keys.add(KeySchemaElement.builder().attributeName("dateUpdated").keyType(KeyType.RANGE).build());

                    final GlobalSecondaryIndex gsi1 = GlobalSecondaryIndex.builder()
                                .indexName("ItemId_userId")
                                .keySchema(gsi1Keys)
                                .provisionedThroughput(ProvisionedThroughput.builder().writeCapacityUnits(5L).readCapacityUnits(5L).build()).build();
                    
                    final GlobalSecondaryIndex gsi2 = GlobalSecondaryIndex.builder()
                                .indexName("UserId_dateUpdated")
                                .keySchema(gsi2Keys)
                                .provisionedThroughput(ProvisionedThroughput.builder().writeCapacityUnits(5L).readCapacityUnits(5L).build()).build();

                    final CreateTableRequest createTableRequest = CreateTableRequest.builder()
                                                    .tableName("UserBasket")
                                                    .attributeDefinitions(attributeDefinitions)
                                                    .keySchema(primaryElements)
                                                    .globalSecondaryIndexes(gsi1, gsi2)
                                                    .provisionedThroughput(ProvisionedThroughput.builder().writeCapacityUnits(5L).readCapacityUnits(9L).build())
                                                    .build();

                    Mono.fromFuture(dynamoDBAsyncClient.createTable(createTableRequest)).subscribe();
                    return Mono.empty();
                }
                else
                    return Mono.error(e);
            }).block();
    }

    public Mono<PutItemResponse> putItem(final UserBasketItem item) {
        Map<String, AttributeValue> values = new HashMap<>();
        values.put("userId", AttributeValue.builder().s(item.getUserId()).build());
        values.put("itemId", AttributeValue.builder().s(item.getItemId()).build());
        values.put("location", AttributeValue.builder().s(item.getLocation().getItemLocation()).build());
        values.put("count", AttributeValue.builder().n(String.valueOf(item.getCount())).build());
        values.put("userType", AttributeValue.builder().s(item.getUserType().getUserType()).build());

        if (item.getMeasurement() != null) {
            values.put("measurement", AttributeValue.builder().n(String.valueOf(item.getMeasurement())).build());
        }
        Date dateUpdated = new Date();
        values.put("dateUpdated", AttributeValue.builder().s(dateTimeFormat.format(dateUpdated)).build());
        values.put("dateUpdated_itemId", AttributeValue.builder().s(dateTimeFormat.format(dateUpdated) + "_" + item.getItemId()).build());

        PutItemRequest request = PutItemRequest.builder()
            .item(values).tableName("UserBasket").build();
        
        return Mono.fromFuture(dynamoDBAsyncClient.putItem(request));
    }

    public Mono<QueryResponse> findByUserId(String userId) {
        QueryRequest request = QueryRequest.builder().tableName("UserBasket").keyConditionExpression("userId = :" + userId).build();
        return Mono.fromFuture(dynamoDBAsyncClient.query(request));
    }
}