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
		
	static Stream<Arguments> validBloodSugarLevels(){
		return Stream.of(
				Arguments.of(false, new double[] {0.0, 0.0, 0.0}),
				Arguments.of(false, new double[] {2.5, 3.5, 4.5}),
				Arguments.of(false, new double[] {2.5, 3.5, 4.5, 5.5, 6.5}),
				Arguments.of(false, new double[] {2.5, 3.5, 4.5, 5.5, 6.5, 7.5}),
				Arguments.of(true, new double[] {9.5, 10.5, 10.1}),
				Arguments.of(true, new double[] {11, 11, 12, 12, 13}),
				Arguments.of(true, new double[] {18, 19, 20, 19, 18, 17})
				);	
	}
	
	static Stream<Arguments> invalidBloodSugarLevels() {
		 return Stream.of(
				 Arguments.of( -0.2, new double[] { -0.2, 4, 4, 5, 6 } ),
				 Arguments.of( -0.1, new double[] { 5, -0.1, 3, 4, 10} ),
				 Arguments.of( 20.1, new double[] { 3, 3, 5, 4, 20.1 } ),
				 Arguments.of( 20.2, new double[] { 5, 3, 3, 4, 20.2 } )
		  );
	} 
	
	
	static Stream<Arguments> invalidNumBloodSugarLevels(){
		return Stream.of(
				Arguments.of(0, new double[] {}),
				Arguments.of(1, new double[] {2.5}),
				Arguments.of(2, new double[] {2.5, 3.5}),
				Arguments.of(7, new double[] {2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5})
				);	
	}
	
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
	
	@ParameterizedTest
	@MethodSource("invalidBloodSugarLevels")
	void testInvalidBloodSugarLevels(double bloodSugarLevel, double[] sugarLevels) {
		Throwable exception = assertThrows(IllegalArgumentException.class, () 
				-> {bloodCheck.checkBloodSugarForDiabetesType2(sugarLevels);});
		assertEquals("Sugar Level must be between 0.0 and 20.0: " + bloodSugarLevel + " not allowed.", exception.getMessage());
	}
	
	@ParameterizedTest
	@MethodSource("validBloodSugarLevels")
	void testAverageForDiabetesType2(boolean result, double[] sugarLevels) {
		assertEquals(result, bloodCheck.checkBloodSugarForDiabetesType2(sugarLevels));
	}
	
	
	@ParameterizedTest(name = "Sugar Level must be between 0.0 and 20.0: {0} not allowed.")
	@MethodSource("invalidNumBloodSugarLevels")
	void testMultipleSafeLevelsInvalidLength(int num, double[] sugarLevels) {
		Throwable exception = assertThrows(IllegalArgumentException.class, ()
				-> {bloodCheck.checkBloodSugarForDiabetesType2(sugarLevels);});
		assertEquals("Sugar Level readings must be between 3 and 6: " + num + " is not acceptable.", exception.getMessage());
	}
	
	
}
