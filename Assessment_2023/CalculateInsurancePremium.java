package com.tus.test;

public class CalculateInsurancePremium {

	public static double calculatePremium(int age, boolean hasAccidents, int[] carValues) throws IllegalArgumentException {
		double policy_cost = 0.0;
		double total = 0.0;
		
		if (carValues.length < 1 || carValues.length > 3) {
			throw new IllegalArgumentException(carValues.length + " car values provided, should be one to three");
		}
		
		for (double carValue : carValues) {
			if (age >= 18 && age <= 25) {
				policy_cost = (carValue * 0.05) * 1.5;
			} 
			else if (age > 25 && age <= 60) {
				policy_cost = carValue * 0.05;
			}
			else if (age > 60 && age <= 95) {
				policy_cost = (carValue * 0.05) * 1.25;
			}
			else {
				throw new IllegalArgumentException("Age must be in range 18 to 95: " + age + " not allowed.");
			}
			if (hasAccidents) {
				policy_cost = policy_cost + (policy_cost * 0.2);
			}
			total += policy_cost;
		}
		return total;
	}
	
	
}
