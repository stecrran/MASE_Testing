package edu.tus.employee.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tus.employee.service.EmployeeService;
import edu.tus.employee.errors.ErrorMessage;
import edu.tus.employee.errors.ErrorMessages;
import edu.tus.employee.exception.EmployeeValidationException;
import edu.tus.employee.model.Employee;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {
	
	@Autowired
	EmployeeController employeeController;
	
	@MockBean
	EmployeeService employeeService;
	

	@Test
	public void addEmployeeTestSuccess() throws EmployeeValidationException 
	{
		// create a test Employee object
		Employee employee = buildEmployee();
		Employee savedEmployee = buildEmployee();
		savedEmployee.setId(1L);
		// Mock the createEmployee method in the Service layer so it returns a saved employee with an ID
		when(employeeService.createEmployee(employee)).thenReturn(savedEmployee);
		// call actual Controller method directly with addEmployee 
		ResponseEntity<Employee> response = employeeController.addEmployee(employee);
		// test the httpstatus code return is "CREATED"
		assertEquals(response.getStatusCode(), HttpStatus.CREATED); // HTTP status code = 201
		// test the details for the employee object in the message body is correct
		// i.e. cast the body of the HTTP response (from the Controller) into an Employee object, so it can be inspected
		Employee employeeAdded = response.getBody();
		//employeeAdded.getId(); // this is not needed
		assertEquals(1L, employeeAdded.getId());
		assertTrue(employeeAdded.equals(savedEmployee));
	}
	
	
	@Test
	public void addEmployeeTestFail() throws EmployeeValidationException
	{
		Employee employee = buildEmployee();
		// Mock the createEmployee method to throw an EmployeeValidationException with EMPTY_FIELDS method
		when(employeeService.createEmployee(employee)).thenThrow(new EmployeeValidationException(ErrorMessages.EMPTY_FIELDS.getMsg()));
		
		// call the addEmployee method in the controller - testing the controller logic directly (not a full HTTP endpoint)
		ResponseEntity<ErrorMessage> response = employeeController.addEmployee(employee);
		
        // test that the response is "BAD_REQUEST"
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		// tests that the correct error message is received in the request body 
		assertEquals(ErrorMessages.EMPTY_FIELDS.getMsg(), response.getBody().getErrorMessage());
	}
	
	
	Employee buildEmployee() {
		Employee employee = new Employee();
		employee.setAge(20);
		employee.setFirstName("Joe");
		employee.setLastName("Bloggs");
		employee.setEmailAddress("Joe@gmail.com");
		return employee;
	}

}
