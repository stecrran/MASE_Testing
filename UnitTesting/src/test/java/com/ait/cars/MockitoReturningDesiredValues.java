package com.ait.cars;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.management.RuntimeErrorException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MockitoReturningDesiredValues {
	
	private Car myFerrari = mock(Car.class);
	
	@Test
	public void testStubbing() {
		assertFalse(myFerrari.needsFuel());
		when(myFerrari.needsFuel()).thenReturn(true);
		assertTrue(myFerrari.needsFuel());
	}
	
	@Test
	@DisplayName("throwing an exception")
	public void testStubbingException() {
		when(myFerrari.needsFuel()).thenThrow(new RuntimeException());
		assertThrows(RuntimeException.class, () -> 
		{myFerrari.needsFuel();});
	}
}

