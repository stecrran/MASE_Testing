package edu.tus.car.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.tus.car.exception.CarException;
import edu.tus.car.model.Car;
import edu.tus.car.service.CarService;
import edu.tus.car.errors.ErrorMessage;

@RestController
@RequestMapping("/api/cars")
public class CarController {

	@Autowired
	CarService carService;

	@PostMapping
	public ResponseEntity addCar(@RequestBody Car car) {
		try {
			Car savedCar = carService.createCar(car);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);
		} catch (CarException e) {
			ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

}
