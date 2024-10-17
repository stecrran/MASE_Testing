package com.tus.parking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class CarParkFeeTest {

	private CarParkFee parkingFee;

	@BeforeEach
	public void setUp() {
		parkingFee = new CarParkFee();
	}

	@ParameterizedTest
	@ValueSource(ints = { -1, 25, 26 })
	public void outOfRangeValueTest(int hours) {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
			parkingFee.calculateDailyFee(hours);
		});
		assertEquals("Incorrect number of hours: " + hours + " Number must be 0 to 24.", exception.getMessage());
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/car-data.csv")
	public void inRangeValuesTest(int hour, double rate) {
		assertEquals(rate, parkingFee.calculateDailyFee(hour), 0.0);
	}

	static Stream<Arguments> invalidParkingDuration() {
		return Stream.of(
				Arguments.of(new int[] {}), 
				Arguments.of(new int[] { -1 }), 
				Arguments.of(new int[] { -1, -2 }),
				Arguments.of(new int[] { -1, -2, -3 }), 
				Arguments.of(new int[] { -1, -2, -3, 25 }),
				Arguments.of(new int[] { -1, -2, -3, 25, 26, 27 }));
	}

	@ParameterizedTest(name = "Incorrect number of days: {0}. Use values for all 5 days.")
	@MethodSource("invalidParkingDuration")
	void testInvalidParkingValuesLength(int[] parkingHours) {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
			parkingFee.calculateWeeklyFee(parkingHours);});
		for (int hours : parkingHours) {
			Throwable exceptionHours = assertThrows(IllegalArgumentException.class, () -> {
				parkingFee.calculateDailyFee(hours);});
			assertEquals("Incorrect number of hours: " + hours + " Number must be 0 to 24.", exceptionHours.getMessage());
		}
	}
	
	static Stream<Arguments> validParkingDuration() {
		return Stream.of(
				Arguments.of(10, new int[] { 1, 2, 3, 4, 5 }),
				Arguments.of(30, new int[] { 10, 20, 10, 20, 10 }),
				Arguments.of(20, new int[] { 10, 5, 6, 12, 14 }));
	}
	
	@ParameterizedTest
	@MethodSource("validParkingDuration")
	void testValidParkingValuesLength(double fee, int[] parkingHours) {
		assertEquals(fee, parkingFee.calculateWeeklyFee(parkingHours),0.0);
		}
	

}
