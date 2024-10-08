package com.tus.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

public class CalculateInsurancePremiumTest {

	private final int[] CAR_VALUES = {2000, 3000, 5000};
	
	@ParameterizedTest(name="ages = {0} should be invalid")
	@ValueSource(ints = {16, 17, 96, 97})
	void testInvalidAge(int age) {
		Throwable exception = assertThrows(IllegalArgumentException.class, () 
				-> {CalculateInsurancePremium.calculatePremium(age, false, CAR_VALUES);});
		assertEquals("Age must be in range 18 to 95: " + age + " not allowed.", exception.getMessage());
	}
	
	// using CSV file
	@ParameterizedTest
	@CsvFileSource(resources="/test.csv")
	void insuranceCostTestCsv(int age, boolean hasAccidents, double totalPolicy) {
		assertEquals(totalPolicy, CalculateInsurancePremium.calculatePremium(age, hasAccidents, CAR_VALUES));
	}
}
