package com.tus.sugarlevel;

public class BloodCheck {
	
	public String checkBloodSugarLevel(double sugarLevel) throws IllegalArgumentException{
		String result = "";
		if (sugarLevel >= 0.0 && sugarLevel <= 5.0) {
			result = "LOW";
		}
		else {
			throw new IllegalArgumentException("Sugar Level must be between 0.0 and 20.0: " + sugarLevel + " not allowed.");
		}
		return result;
	}

}
