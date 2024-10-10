package com.ait.cars;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;

import com.ait.cars.Car;

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
		verify(myFerrari,new Times(2)).needsFuel();

	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultBehaviourOfTestDouble() {
		assertFalse(myFerrari.needsFuel()); // new test double should return false as boolean
		assertEquals(0.0, myFerrari.getEngineTemperature(),0.01);
	}

}

