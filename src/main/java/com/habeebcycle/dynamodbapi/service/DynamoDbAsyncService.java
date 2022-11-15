package com.habeebcycle.dynamodbapi.service;

import com.habeebcycle.dynamodbapi.config.DynamoDbConfig;
import com.habeebcycle.dynamodbapi.entity.Employee;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;

import java.util.UUID;

@Service
public class DynamoDbAsyncService {

    private final DynamoDbAsyncTable<Employee> employeeTable;
    private final DynamoDbConfig dynamoDbConfig;

    public DynamoDbAsyncService(final DynamoDbConfig dynamoDbConfig) throws Exception {
        DynamoDbEnhancedAsyncClient enhancedAsyncClient = dynamoDbConfig.dynamoDbEnhancedAsyncClient();
        this.dynamoDbConfig = dynamoDbConfig;
        this.employeeTable = enhancedAsyncClient.table("employees", TableSchema.fromBean(Employee.class));
    }

    public Mono<Boolean> createTable() throws Exception {
        return Mono.fromFuture(dynamoDbConfig.dynamoDbAsyncClient().listTables())
                .filter(t -> t.tableNames().contains("employees"))
                .hasElement().flatMap(b -> {
                    if(Boolean.FALSE.equals(b)) {
                        return Mono.fromFuture(
                                employeeTable.createTable(x -> x.provisionedThroughput(
                                        ProvisionedThroughput.builder()
                                                .writeCapacityUnits(10L)
                                                .readCapacityUnits(10L).build())))
                                .thenReturn(true);
                    }else {
                        return Mono.just(true);
                    }
                });
    }

    public Mono<Employee> addEmployee(Employee employee) throws Exception {
        return createTable().flatMap(b -> {
            System.out.println(b);
            if(Boolean.TRUE.equals(b)) {
                employee.setEmployeeId(UUID.randomUUID().toString());
                return Mono.fromFuture(employeeTable.putItem(employee))
                        .map(process -> employee)
                        .thenReturn(employee);
            }
            return Mono.just(employee);
        });
    }

    public Flux<Employee> getAllEmployees() {
        return Flux.from(employeeTable.scan().items());
    }

    public Mono<Employee> getEmployee(String employeeId) {
        return Mono.fromFuture(employeeTable.getItem(Key.builder().partitionValue(employeeId).build()));
    }

    public Mono<Employee> deleteEmployee(String employeeId) {
        return Mono.fromFuture(employeeTable.deleteItem(Key.builder().partitionValue(employeeId).build()));
    }
}
