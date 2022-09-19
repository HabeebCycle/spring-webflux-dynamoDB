package com.habeebcycle.dynamodbapi;

import com.habeebcycle.dynamodbapi.entity.Department;
import com.habeebcycle.dynamodbapi.entity.Employee;
import com.habeebcycle.dynamodbapi.repository.DynamoDbRepository;
import com.habeebcycle.dynamodbapi.service.DynamoDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.UUID;

@SpringBootApplication
public class DynamodbApiApplication {

	@Autowired
	private DynamoDbService service;

	@Autowired
	private DynamoDbRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(DynamodbApiApplication.class, args);
	}

	@PostConstruct
	public void testDynamoDb() {
		System.setProperty("sqlite4java.library.path", "target/native-libs");
		String response = service.createTable("employees", "employeeId");
		System.out.println(response);
		/*System.out.println("MY RESPONSE 1");
		System.out.println(response);
		System.out.println("MY RESPONSE 2");
		System.out.println(service.listTables());*/
		/*Department dep = new Department();
		dep.setDepartmentCode("CCS-011");
		dep.setDepartmentName("Centre for Consumer Security");

		Employee emp = new Employee();
		emp.setFirstName("Habeeb");
		emp.setLastName("Okunade");
		emp.setEmail("habeeb@habeeb.com.au");
		emp.setDepartment(dep);
		emp.setEmployeeId(UUID.randomUUID().toString());

		Employee savedEmployee = repository.addEmployee(emp);
		System.out.println(savedEmployee.getDepartment().getDepartmentCode());
		System.out.println(savedEmployee.getVersion());*/

	}

}
