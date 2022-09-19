package com.habeebcycle.dynamodbapi.service;

import com.habeebcycle.dynamodbapi.config.DynamoDbConfig;
import com.habeebcycle.dynamodbapi.entity.Employee;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;

import java.util.UUID;

@Service
public class DynamoDbAsyncService {

    private final DynamoDbAsyncTable<Employee> employeeTable;

    public DynamoDbAsyncService(final DynamoDbConfig dynamoDbConfig) throws Exception {
        DynamoDbEnhancedAsyncClient enhancedAsyncClient = dynamoDbConfig.dynamoDbEnhancedAsyncClient();
        this.employeeTable = enhancedAsyncClient.table("employees", TableSchema.fromBean(Employee.class));
    }

    public Mono<Employee> addEmployee(Employee employee) {
        employee.setEmployeeId(UUID.randomUUID().toString());

        return Mono.fromFuture(employeeTable.putItem(employee))
                .map(process -> employee)
                .thenReturn(employee);
    }

    public Flux<Employee> getAllEmployees() {
        return Flux.from(employeeTable.scan().items());
    }

    public Mono<Employee> getEmployee(String employeeId) {
        return Mono.fromFuture(employeeTable.getItem(Key.builder().partitionValue(employeeId).build()));
    }
}
