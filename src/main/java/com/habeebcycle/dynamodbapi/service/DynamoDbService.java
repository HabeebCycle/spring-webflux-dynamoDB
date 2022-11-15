package com.habeebcycle.dynamodbapi.service;

import com.habeebcycle.dynamodbapi.config.DynamoDbConfig;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import java.util.List;

@Service
public class DynamoDbService {

    private final DynamoDbClient client;

    public DynamoDbService(final DynamoDbConfig dynamoDbConfig) throws Exception {
        this.client = dynamoDbConfig.dynamoDbClient();
    }

    public String createTable(String tableName, String keyName) {
        DynamoDbWaiter dbWaiter = client.waiter();
        var request = CreateTableRequest.builder()
            .attributeDefinitions(attribute -> attribute
                .attributeName(keyName)
                .attributeType(ScalarAttributeType.S).build())
            .keySchema(key -> key
                .attributeName(keyName)
                .keyType(KeyType.HASH).build())//.keySchema(key -> key.attributeName("sortKey").keyType(KeyType.RANGE).build())
            .provisionedThroughput(provision -> provision
                .readCapacityUnits(10L)
                .writeCapacityUnits(10L).build())
            .tableName(tableName)
            .build();

        CreateTableResponse response = client.createTable(request);

        DescribeTableRequest tableRequest = DescribeTableRequest.builder()
            .tableName(tableName).build();

        // Wait until the Amazon DynamoDB table is created.
        WaiterResponse<DescribeTableResponse> waiterResponse = dbWaiter.waitUntilTableExists(tableRequest);
        System.out.println("INTERNAL WAITING");
        waiterResponse.matched().response().ifPresent(System.out::println);

        return response.tableDescription().tableName();
    }

    public List<String> listTables() {
        System.out.println("LIST TABLES");
        return client.listTables().tableNames();
    }
}
