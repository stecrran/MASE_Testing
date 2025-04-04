package edu.tus.employee.controller;

import edu.tus.employee.errors.ErrorMessages;
import edu.tus.employee.exception.EmployeeValidationException;
import edu.tus.employee.model.Employee;
import edu.tus.employee.service.EmployeeService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeAppIT {
	
	@Value(value="${local.server.port}")
	private int port;
	
	TestRestTemplate restTemplate;
	HttpHeaders headers;
	
	@BeforeEach
	public void setup() {
		restTemplate =new TestRestTemplate();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@Test
	@Sql({"/testdata.sql"})
	public void addEmployeeSuccessIntTest()
	{
		Employee employee = buildEmployee();
		//Use TestRestTemplate to call a post with valid data
		HttpEntity<Employee> request = new HttpEntity<>(employee,headers);
		// Test that the response code is "CREATED"
		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/api/employees", request, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		// Note the testdata.sql deletes an employee with email "joe@gmail.com"
	}
	
	@Test
	@Sql({"/testdata.sql"})
	public void addEmployeeEmptyFieldIntTest()
	{
		Employee employee = buildEmployee();
		employee.setFirstName("");
		//Use TestRestTemplate to post data where firstName is empty
		HttpEntity<Employee> request = new HttpEntity<>(employee, headers);
		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:"+ port + "/api/employees", request, String.class);
		//Test that status code BAD_REQUEST is returned
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().contains(ErrorMessages.EMPTY_FIELDS.getMsg()));
	}
	
	@Test
	@Sql({"/testdata.sql"})
	public void addEmployeeEmailAlreadyExistsIntTest() throws EmployeeValidationException
	{
		Employee employee1 = buildEmployee();
		Employee employee2 = buildEmployee();
		employee1.setEmailAddress("joe@gmail.com");
		employee2.setEmailAddress("joe@gmail.com");
		//Use TestRestTemplate to add an employee with existing email address
		HttpEntity<Employee> request1 = new HttpEntity<>(employee1, headers);
		ResponseEntity<String> response1 = restTemplate.postForEntity("http://localhost:"+ port + "/api/employees", request1, String.class);
		
		HttpEntity<Employee> request2 = new HttpEntity<>(employee2, headers);
		ResponseEntity<String> response2 = restTemplate.postForEntity("http://localhost:"+ port + "/api/employees", request2, String.class);
		//Test that status code BAD_REQUEST is returned
		assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
		assertTrue(response2.getBody().contains(ErrorMessages.ALREADY_EXISTS.getMsg()));
	}
	
	@Test
	@Sql({"/testdata.sql"})
	public void addEmployeeAgeInvalidIntTest()
	{
		Employee employee = buildEmployee();
		employee.setAge(17);
		//Use TestRestTemplate to add an employee with age not in correct range
		HttpEntity<Employee> request = new HttpEntity<>(employee, headers);
		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:"+ port + "/api/employees", request, String.class);
		//Test that status code BAD_REQUEST is returned
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().contains(ErrorMessages.INVALID_AGE.getMsg()));
	}
	
	Employee buildEmployee() {
		Employee employee = new Employee();
		employee.setAge(20);
		employee.setFirstName("Joe");
		employee.setLastName("Bloggs");
		employee.setEmailAddress("joe@gmail.com");
		return employee;
	}

}
