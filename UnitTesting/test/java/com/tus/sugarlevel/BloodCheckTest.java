package com.tus.sugarlevel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

public class BloodCheckTest {
	
	BloodCheck bloodCheck = new BloodCheck();
	
	@Test
	void lowBloodSugar() {
		assertEquals("LOW", bloodCheck.checkBloodSugarLevel(3.0));
	}

	@Test
	void outOfRange() {
		Throwable Exception = assertThrows(IllegalArgumentException.class, ()
				-> {bloodCheck.checkBloodSugarLevel(20.1);});
		assertEquals("Sugar Level must be between 0.0 and 20.0: 20.1 not allowed.", Exception.getMessage());
	}
	
}
