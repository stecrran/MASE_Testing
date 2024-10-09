package com.tus.leapyear;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class DateUtilsTest {
	

	@Test
	void normalLeapYearIsLeap() {
		assertTrue(DateUtils.isLeapYear(1992));
	}
	
	@Test
	void normalLeapYearIsLeapExample2() {
		assertTrue(DateUtils.isLeapYear(1996));
	}
	
	@Test
	void nonLeapYearIsNotLeap() {
		assertFalse(DateUtils.isLeapYear(1991));
	}
	
	@Test
	void centuryYearsAreNotLeap() {
		assertFalse(DateUtils.isLeapYear(1900));
	}
	
	@Test
	void year200WasLeap() {
		assertTrue(DateUtils.isLeapYear(2000));
	}
}
