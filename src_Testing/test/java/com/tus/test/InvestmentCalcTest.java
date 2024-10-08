package com.tus.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class InvestmentCalcTest {
	private static final int VALID_TERM = 4;
	private static final int VALID_INVESTMENT = 2000;
	
	//Example using CsvSource
	@ParameterizedTest(name = "Initial amount {0} for {1} year(s) should result in {2}")
	@CsvSource({"1000,3,1061.21", "1001,4,1083.51","2998,5,3310.03","2999,6,3377.36","3000,7,4221.30","3001,8,4433.84",
		"4998,9,7753.54","4999,10,8142.84","5000,3,6125.22","5001,4,6555.29","9999,6,15005.80","10000,7,16057.81"})
	void testGetInvestmentValue(int startAmount, int term, double finalAmount) {
		assertEquals(finalAmount, InvestmentCalc.calculateInvestmentValue(term, startAmount));
	}
	
	// or using CSV file
	@ParameterizedTest
	@CsvFileSource(resources="/investData.csv")
	void investmentCalcTestCsv(int startAmount, int term, double finalAmount) {
		assertEquals(finalAmount, InvestmentCalc.calculateInvestmentValue(term, startAmount));
	}
	
	@ParameterizedTest(name="Amount = {0} should be invalid")
	@ValueSource(doubles = {998.0, 999.0, 10001.0, 10002.0})
	void testInvalidAmount(double startingAmount) {
		Throwable exception = assertThrows(IllegalArgumentException.class, () 
				-> {InvestmentCalc.calculateInvestmentValue(VALID_TERM, startingAmount);});
		assertEquals("illegal investment amount: ["+startingAmount+"]", exception.getMessage());
	}
	
	@ParameterizedTest(name="Term =  {0} years should be invalid")
	@ValueSource(ints= {1, 2, 11, 12})
	void testInvalidTerm(int term) {
		Throwable exception = assertThrows(IllegalArgumentException.class, () 
				-> {InvestmentCalc.calculateInvestmentValue(term, VALID_INVESTMENT);});
		assertEquals("illegal investment term: ["+term+"]",exception.getMessage());
	}
	

}
