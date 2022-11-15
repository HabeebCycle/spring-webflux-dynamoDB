package com.habeebcycle.dynamodbapi;

import com.habeebcycle.dynamodbapi.entity.Department;
import com.habeebcycle.dynamodbapi.entity.Employee;
import com.habeebcycle.dynamodbapi.repository.DynamoDbRepository;
import com.habeebcycle.dynamodbapi.service.DynamoDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@SpringBootApplication
public class DynamodbApiApplication {

	@Value("${sqlite4java.library.path}")
	private String myPath;
	@Autowired
	private DynamoDbService service;

	@Autowired
	private DynamoDbRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(DynamodbApiApplication.class, args);
	}

	@Bean
	public void setUp() throws IOException {
		Resource resource = new ClassPathResource("native-libs");
		String filePath = resource.getFile().getPath();
		System.setProperty("sqlite4java.library.path", filePath);
	}

	@PostConstruct
	public void testDynamoDb() throws IOException {
		System.out.println(myPath);//String absolutePath = new File(MainCore.class.getClassLoader().getResource("chromedriver-76.0.3809.68.exe").getFile()).getAbsolutePath();
		Resource resource = new ClassPathResource("native-libs");
		String filePath = resource.getFile().getPath();
		System.setProperty("sqlite4java.library.path", filePath);
		System.out.println(System.getProperty("sqlite4java.library.path"));
		//String response = service.createTable("employees", "employeeId");
		//System.out.println(response);
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
