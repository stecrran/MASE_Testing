package edu.tus.car.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import edu.tus.car.model.Car;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Not necessary to use random port 
class CarAppIT {
	
	@Value(value="${local.server.port}")
	private int port;
	//To Do Define instance variables
	TestRestTemplate restTemplate;
	HttpHeaders headers;
	
	
	@BeforeEach
	public void setup() {
		//TO DO
		restTemplate =new TestRestTemplate();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@Test
	@Sql({"/import.sql"})
	public void addCarSuccessIntTest()
	{
		
		//use restTeplate to post a car object with valid values
		Car car = buildCar();
		HttpEntity<Car> request = new HttpEntity<>(car,headers);
		//test that  HttpStatus.CREATED is returned
		ResponseEntity<String>	response =	restTemplate.postForEntity("http://localhost:"+port+"/api/cars", request, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	
	@Test
	@Sql({"/import.sql"})
	public void addCarInvalidYearTest()
	{
		Car car = buildCar();
		car.setYear(2014);
		//use restTeplate to post a car object with year too low e.g. 2014
		HttpEntity<Car> request = new HttpEntity<>(car,headers);
		ResponseEntity<String>	response =	restTemplate.postForEntity("http://localhost:"+port+"/api/cars", request, String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
				//test that  HttpStatus.BAD_REQUEST is returned
	}
	
	@Test
	@Sql({"/import.sql"})

	public void addCarInvalidMakeTest()
	{
		//use restTeplate to post a car object with invalid make e.g. "Ford"
		//test that  HttpStatus.BAD_REQUEST is returned
		Car car = buildCar();
		car.setMake("Ford");
		HttpEntity<Car> request = new HttpEntity<>(car,headers);
		ResponseEntity<String>	response =	restTemplate.postForEntity("http://localhost:"+port+"/api/cars", request, String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	
	}
	
	@Test
	@Sql({"/import.sql"})
	public void addCarInvalidModelTest()
	{
		//use restTeplate to post a car object with valid make but invalid model e.g. C180
		//test that  HttpStatus.BAD_REQUEST is returned
		Car car = buildCar();
		car.setMake("C180");
		HttpEntity<Car> request = new HttpEntity<>(car,headers);
		ResponseEntity<String>	response =	restTemplate.postForEntity("http://localhost:"+port+"/api/cars", request, String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		
	}
	
	
	
	Car buildCar() {
		Car car = new Car();
		car.setMake("Mercedes");
		car.setModel("E220");
		car.setYear(2021);
		car.setColor("blue");
		return car;
	}

}
