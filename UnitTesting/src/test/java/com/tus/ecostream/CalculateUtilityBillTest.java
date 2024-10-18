package com.tus.ecostream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class CalculateUtilityBillTest {

	CalculateUtilityBill utilityBill;

	final boolean hasSolar = true;

	static Stream<Arguments> invalidUsagesLength() {
		return Stream.of(Arguments.of(0, new int[] {}), 
				Arguments.of(1, new int[] { 0 }),
				Arguments.of(2, new int[] { 0, 1 }), 
				Arguments.of(4, new int[] { 0, 1, 2, 3 }),
				Arguments.of(5, new int[] { 0, 1, 2, 3, 4 }));
	}

	@BeforeEach
	public void setUp() {
		utilityBill = new CalculateUtilityBill();
	}

	@ParameterizedTest
	@ValueSource(ints = { -2, -1 })
	void testInvalidUnitValuesElectric(int unit) {
		int[] unitsTest = new int[] { unit, 1, 1 };
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
			utilityBill.calculateBill(unitsTest, hasSolar);
		});
		assertEquals("Usage cannot be negative: " + unit + " KWh of electricity.", exception.getMessage());
	}

	@ParameterizedTest
	@ValueSource(ints = { -2, -1 })
	void testInvalidUnitValuesWater(int unit) {
		int[] unitsTest = new int[] { 1, unit, 1 };
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
			utilityBill.calculateBill(unitsTest, hasSolar);
		});
		assertEquals("Usage cannot be negative: " + unit + " cubic meters of water.", exception.getMessage());
	}

	@ParameterizedTest
	@ValueSource(ints = { -2, -1 })
	void testInvalidUnitValuesGas(int unit) {
		int[] unitsTest = new int[] { 1, 1, unit };
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
			utilityBill.calculateBill(unitsTest, hasSolar);
		});
		assertEquals("Usage cannot be negative: " + unit + " cubic meters of gas.", exception.getMessage());
	}

	@ParameterizedTest
	@MethodSource("invalidUsagesLength")
	void testValidUsagesLength(int numValues, int[] values) {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
			utilityBill.calculateBill(values, hasSolar);
		});
		assertEquals("Invalid number of usage values: expected 3, found " + values.length, exception.getMessage());
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/test_bills.csv", numLinesToSkip = 1)
	void testValidCalues(int elec, int water, int gas, boolean solar, double total) {
		int[] utility = new int[] { elec, water, gas };

		assertEquals(total, utilityBill.calculateBill(utility, solar), 0.0);
	}

}
