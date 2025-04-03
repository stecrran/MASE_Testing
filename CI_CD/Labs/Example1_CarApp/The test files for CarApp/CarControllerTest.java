package edu.tus.car.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import edu.tus.car.exception.CarValidationException;
import edu.tus.car.model.Car;
import edu.tus.car.service.CarService;
import edu.tus.car.errors.ErrorMessage;
import edu.tus.car.errors.ErrorMessages;

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerTest {

	@Autowired
	CarController carController;
	
	@MockBean
	CarService carService;
	
	@Captor
	ArgumentCaptor<Car> captor;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void addCarTestSuccess() throws Exception
	{
		// testing addCar method
		Car car = buildCar();
		ObjectMapper map =new ObjectMapper();
		String jsonString = map.writeValueAsString(car);
		//mock the service layer
		when(carService.createCar(car)).thenReturn(car);
		// test for HttpStatus.CREATED
		// test car object returned has correct values
		this.mockMvc.perform(post("/api/cars").contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)).andDo(print()).andExpect(status().isCreated());
				
	}
	
	@Test
	public void addCarTestFail() throws CarValidationException
	{
		// testing addCar method
		Car car = buildCar();
		when(carService.createCar(car)).thenThrow(new CarValidationException(ErrorMessages.INVALID_MAKE_MODEL.getMsg()));
				//mock the service layer - exception INVALID_MAKE_MODEL
				// test for HttpStatus.BAD_REQUEST
		ResponseEntity <ErrorMessage> response = carController.addCar(car);
				// test returned errormessage is correct
		  assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		  assertEquals(ErrorMessages.INVALID_MAKE_MODEL.getMsg(), response.getBody().getErrorMessage());
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
