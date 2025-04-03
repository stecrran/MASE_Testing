package edu.tus.car.errors;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.tus.car.model.Car;


class ErrorValidationTest {
	ErrorValidation errorValidation;
	Car car;

	@BeforeEach
	void setUp() {
		errorValidation = new ErrorValidation();
		car = buildCar();
	}

	@Test
	void testMakeAndModelAllowed() {
		//test checkMakeAndModelNotAllowed
		car = buildCar();
		assertFalse(errorValidation.checkMakeAndModelNotAllowed(car));
	}
	
	@Test
	void testMakeNotAllowed() {
		//test checkMakeAndModelNotAllowed
		car = buildCar();
		car.setMake("");
		assertTrue(errorValidation.checkMakeAndModelNotAllowed(car));
	}
	
	@Test
	void testMakeAllowedModelNotAllowed() {
		//test checkMakeAndModelNotAllowed
		car = buildCar();
		car.setModel("");
		assertTrue (errorValidation.checkMakeAndModelNotAllowed(car));
	}
	
	@Test
	void testValidCarYear() {
		//test yearOK
		car= buildCar();
		assertFalse (errorValidation.yearNotOK(car));
	}
	
	@Test
	void testInValidCarYear() {
		//test yearNotOK - too old
		car= buildCar();
		car.setYear(2019);
		assertTrue (errorValidation.yearNotOK(car));
		
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
