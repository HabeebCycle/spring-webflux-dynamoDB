package com.habeebcycle.dynamodbapi.repository;

import com.habeebcycle.dynamodbapi.config.DynamoDbConfig;
import com.habeebcycle.dynamodbapi.entity.Employee;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;

import java.util.List;

@Repository
public class DynamoDbRepository {

    private final DynamoDbTable<Employee> employeeTable;

    public DynamoDbRepository(final DynamoDbConfig dynamoDbConfig) throws Exception {
        DynamoDbEnhancedClient enhancedClient = dynamoDbConfig.dynamoDbEnhancedClient();
        this.employeeTable = enhancedClient.table("employees", TableSchema.fromBean(Employee.class));
        //employeeTable.createTable(x -> x.provisionedThroughput(ProvisionedThroughput.builder().writeCapacityUnits(10L).readCapacityUnits(10L).build()));
    }

    public Employee addEmployee(Employee employee) {
        employeeTable.putItem(employee);
        return employee;
    }

    public List<Employee> getAllEmployees() {
        return employeeTable.scan().items().stream().toList();
    }

    public Employee getEmployee(String employeeId) {
        return employeeTable.getItem(builder -> builder.key(k -> k.partitionValue(employeeId).build()));
    }
}
