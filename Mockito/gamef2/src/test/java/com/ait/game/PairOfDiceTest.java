package com.ait.game;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;



public class PairOfDiceTest {
//testing PairOFDice and Die together
	@Test
	public void testDiceRollValuesBetweenLimits() {
		PairOfDice dice=new PairOfDice();
		dice.roll();
		int die1=dice.getValue1();
		int die2=dice.getValue2();
		assertEquals(die1+die2,dice.getSum());
		assertThat(die1, allOf(greaterThan(0), lessThan(7)));
		assertThat(die2, allOf(greaterThan(0), lessThan(7)));
	}

}
