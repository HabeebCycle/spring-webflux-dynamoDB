package com.habeebcycle.dynamodbapi.controller;

import com.habeebcycle.dynamodbapi.entity.Employee;
import com.habeebcycle.dynamodbapi.repository.DynamoDbRepository;
import com.habeebcycle.dynamodbapi.service.DynamoDbAsyncService;
import com.habeebcycle.dynamodbapi.service.DynamoDbService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class DynaboDbController {

    private final DynamoDbRepository repository;
    private final DynamoDbService service;
    private final DynamoDbAsyncService asyncService;

    public DynaboDbController(DynamoDbRepository repository, DynamoDbService service, DynamoDbAsyncService asyncService) {
        this.repository = repository;
        this.service = service;
        this.asyncService = asyncService;
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return repository.getAllEmployees();
    }

    @GetMapping(value = "/async/employees", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Employee> getAllEmployeesAsync() {
        return asyncService.getAllEmployees();
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable String id) {
        return repository.getEmployee(id);
    }

    @GetMapping(value = "/async/employees/{id}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<Employee> getEmployeeAsync(@PathVariable String id) {
        return asyncService.getEmployee(id);
    }

    @PostMapping(value = "/async/employees", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<Employee> addEmployee(@RequestBody Employee employee) throws Exception {
        return asyncService.addEmployee(employee);
    }

    @GetMapping("/tables")
    public List<String> getAllTables() {
        return service.listTables();
    }

}
