package com.tus.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class CalculateInsurancePremiumTest {

	private final int[] CAR_VALUES = {2000, 3000, 5000};
	private final int age = 22;
	
	static Stream<Arguments> invalidCarValueSize(){
		return Stream.of(
			Arguments.of(0, new int[] {}),
			Arguments.of(4, new int[] {1000, 2000, 3000, 4000}),
			Arguments.of(5, new int[] {1000, 2000, 3000, 4000, 5000})
		);
	}
	
	@ParameterizedTest(name="carValue size {0} is invalid")
	@MethodSource("invalidCarValueSize")
	void testInvalidCarArray(int num, int[] carValues) {
		Throwable exception = assertThrows(IllegalArgumentException.class, () 
				-> {CalculateInsurancePremium.calculatePremium(age, false, carValues);});
		assertEquals(carValues.length + " car values provided, should be one to three", exception.getMessage());
	}
	
	
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
