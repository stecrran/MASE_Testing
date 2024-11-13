package com.ait.cars;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;

class CarTest {
	private Car myFerrari = mock(Car.class);
	
	@Test
	void testIfCarIsACar() {
		assertTrue(myFerrari instanceof Car);
		System.out.println(myFerrari.needsFuel());
		when(myFerrari.needsFuel()).thenReturn(true);
		System.out.println(myFerrari.needsFuel());
		System.out.println(myFerrari.getEngineTemperature());
		when(myFerrari.getEngineTemperature()).thenReturn(90.9);
		System.out.println(myFerrari.getEngineTemperature());
		verify(myFerrari,new Times(1)).needsFuel();

	}

}
