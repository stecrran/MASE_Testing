package com.ait.game;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class PlayerTest {

	@Test
	void testNewPlayerCreated() {
		Player player=new Player("Tom");
		assertEquals("Tom",player.getName());
		assertEquals(0,player.getTotalScore());
	}
	
	@Test
	void testPlayerScoreUpdated() {
		Player player=new Player("Tom");
		assertEquals(0,player.getTotalScore());
		player.setTotalScore(5);
		assertEquals(5,player.getTotalScore());
		player.setTotalScore(6);
		assertEquals(11,player.getTotalScore());
	}
	
	@Test
	void testPlayertoString() {
		Player player=new Player("Tom");
		player.setTotalScore(5);
		assertEquals("Current score for Tom is 5",player.toString());
	}

}
