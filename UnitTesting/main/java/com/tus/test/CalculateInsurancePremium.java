package com.tus.test;

public class CalculateInsurancePremium {

	public static void main(String[] args) {
//		CalculateInsurancePremium test = new CalculateInsurancePremium();
//		int[] carValu = { 1234};
//		System.out.println(test.calculatePremium(19, true, carValu));
	}

	public static double calculatePremium(int age, boolean hasAccidents, int[] carValues) throws IllegalArgumentException {
		double policy_cost = 0.0;
		double total = 0.0;
		for (double carValue : carValues) {
			if (age >= 18 && age <= 25) {
				policy_cost = (carValue * 0.05) * 1.5;
			} 
			else if (age > 25 && age <= 60) {
				policy_cost = carValue * 0.05;
			}
			else if (age > 60 && age < 95) {
				policy_cost = (carValue * 0.05) * 1.5;
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
