package com.tus.sugarlevel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class BloodCheckTest {
	
	BloodCheck bloodCheck = new BloodCheck();
	
	@ParameterizedTest
	@ValueSource(doubles = {-0.2, -0.1, 20.1, 20.2})
	void outOfRangeBloodSugar(double bloodSugarLevel) {
		Throwable exception = assertThrows(IllegalArgumentException.class, ()
				-> {bloodCheck.checkBloodSugarLevel(bloodSugarLevel);});
		assertEquals("Sugar Level must be between 0.0 and 20.0: " + bloodSugarLevel + " not allowed.", exception.getMessage());
				
	}

	@ParameterizedTest
	@CsvFileSource(resources="/sugar-data.csv")
	void testValidBloodSugarLevels(double bloodSugarLevel, String result) {
		assertEquals(result, bloodCheck.checkBloodSugarLevel(bloodSugarLevel));
	}
	
	static Stream<Arguments> validBloodSugarLevels(){
		return Stream.of(
				Arguments.of(true, new double[] {2.5, 3.5, 4.5}),
				Arguments.of(true, new double[] {2.5, 3.5, 4.5, 5.5, 6.5}),
				Arguments.of(true, new double[] {2.5, 3.5, 4.5, 5.5, 6.5, 7.5})
				);	
	}
	
	@ParameterizedTest
	@MethodSource("validBloodSugarLevels")
	void testAverageForDiabetesType2(boolean result, double[] sugarLevels) {
		assertFalse(bloodCheck.checkBloodSugarForDiabetesType2(sugarLevels));
	}
	
	static Stream<Arguments> invalidBloodSugarLevels(){
		return Stream.of(
				Arguments.of(0, new double[] {}),
				Arguments.of(1, new double[] {2.5}),
				Arguments.of(2, new double[] {2.5, 3.5}),
				Arguments.of(7, new double[] {2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5})
				);	
	}
	
	
	@ParameterizedTest(name = "Sugar Level must be between 0.0 and 20.0: {0} not allowed.")
	@MethodSource("invalidBloodSugarLevels")
	void testMultipleSafeLevelsInvalidLength(int num, double[] sugarLevels) {
		Throwable exception = assertThrows(IllegalArgumentException.class, ()
				-> {bloodCheck.checkBloodSugarForDiabetesType2(sugarLevels);});
		assertEquals("Sugar Level readings must be between 3 and 6: " + num + " is not acceptable.", exception.getMessage());
	}
	
	
}
