package com.ait.cars;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

public class MockitoVerificationTest {

	private Car myFerrari = mock(Car.class);
	
	@Test 
	public void testVerification() {
		myFerrari.driveTo("Belfast");
		myFerrari.needsFuel();
		verify(myFerrari).driveTo("Belfast");
		verify(myFerrari).needsFuel();
	}
	
	@Test
	public void testVerificationFailure() {
		myFerrari.needsFuel();
		//verify(myFerrari).needsFuel();
		//myFerrari.getEngineTemperature(); // test fails if method is not called
		verify(myFerrari).getEngineTemperature();
	}
}
