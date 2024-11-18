package com.ait.game;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;



class DieTest {

	@RepeatedTest(20)
	void testDieReturnsValueBetweenLimits() {
		Die die=new Die();
		die.roll();
		int value=die.getValue();
		assertThat(value, allOf(greaterThan(0), lessThan(7)));
	}

}
