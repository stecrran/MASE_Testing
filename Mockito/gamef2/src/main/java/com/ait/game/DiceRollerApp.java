package com.ait.game;

public class DiceRollerApp
{
	
    public static void main(String args[])
    {
        
        IOHandler gameIO=new IOHandler();
        PairOfDice pairOfDice= new PairOfDice();
        Game game=new Game(gameIO,pairOfDice);
    	game.start();
    	gameIO.closeScanner();
	}

} 