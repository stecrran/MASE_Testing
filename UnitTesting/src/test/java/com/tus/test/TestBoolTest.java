package com.tus.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matcher;
import static org.hamcrest.Matchers.is; // Import the correct "is" matcher

public class TestBoolTest {

	@ParameterizedTest
	@CsvFileSource(resources="/testDataBool.csv")
	public void isThisEvenTest(int number, boolean isThisEven) {
		assertThat(TestBoolean.isEven(number),is(isThisEven));
	}


}
