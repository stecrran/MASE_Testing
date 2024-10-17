package com.tus.sugarlevel;

public class BloodCheck {

	private static final double LOW = 0.0;
	private static final double OK = 5.0;
	private static final double HIGH = 7.0;
	private static final double ALERT = 10.0;
	private static final double MAX = 20.0;


	public String checkBloodSugarLevel(double sugarLevel) throws IllegalArgumentException {
		String result = "";

		if (sugarLevel < LOW || sugarLevel > MAX) {
			throw new IllegalArgumentException(
					"Sugar Level must be between 0.0 and 20.0: " + sugarLevel + " not allowed.");
		}

		if (sugarLevel <= OK) {
			result = "LOW";
		}

		else if (sugarLevel <= HIGH) {
			result = "OK";
		} else if (sugarLevel <= ALERT) {
			result = "HIGH";
		}

		else {
			result = "ALERT";
		}
		return result;
	}

	public boolean checkBloodSugarForDiabetesType2(double[] sugarLevels) throws IllegalArgumentException {
		double total = 0.0;
		boolean result = false;

		for (double sugarLevel : sugarLevels) {
			if (sugarLevel < LOW || sugarLevel > MAX) {
				throw new IllegalArgumentException(
						"Sugar Level must be between 0.0 and 20.0: " + sugarLevel + " not allowed.");
			}
			total += sugarLevel;
		}

		double average = total / sugarLevels.length;

		if (sugarLevels.length < 3 || sugarLevels.length > 6) {
			throw new IllegalArgumentException(
					"Sugar Level readings must be between 3 and 6: " + sugarLevels.length + " is not acceptable.");
		}

		if (average <= 10.0) {
			result = false;
		}

		else {
			result = true;
		}
		return result;
	}

}