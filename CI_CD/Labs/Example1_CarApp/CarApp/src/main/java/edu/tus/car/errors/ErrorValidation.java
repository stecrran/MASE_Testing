package edu.tus.car.errors;

import java.util.HashMap;


import org.springframework.stereotype.Component;

import edu.tus.car.model.Car;

@Component
public class ErrorValidation {
	
	HashMap<String, String> makeModels;
	
public ErrorValidation() {
	makeModels = new HashMap<String, String>();
	makeModels.put("MERCEDES","E220");
	makeModels.put("AUDI","A4");
	makeModels.put("VOLKSVAGEN","ARTEON");
	makeModels.put("BMW","320");
}
	public boolean checkMakeAndModelNotAllowed(Car car) {
		String model =makeModels.get(car.getMake().toUpperCase());
		return (model==null ||(!model.equals(car.getModel().toUpperCase())));
	}
	
	public boolean yearNotOK(Car car) {
		return ((car.getYear()<2020)); 
	}

}
