package com.ait.game;
public class Game {
    private IOHandler ioHandler;
    private Player player1;
    private Player player2;
    private PairOfDice dice;
    private int numDiceRolls;
    static final int SCORE_TO_WIN = 20;

    // Constructor with dependency injection
    public Game(IOHandler ioHandler, PairOfDice dice) {
        this.ioHandler = ioHandler;
        this.dice = dice;
    }

    public void start() {
        numDiceRolls = 0;

        // display a welcome message
        ioHandler.print("Welcome to the Dice Roller application\n");

        String player1Name = ioHandler.getString("Please enter player one name: ");
        String player2Name = ioHandler.getString("Please enter player two name: ");

        player1 = new Player(player1Name);
        player2 = new Player(player2Name);

        String choice = ioHandler.getLowercaseString("Roll the dice? (y/n): ");

        while (choice.equals("y")) {
            numDiceRolls++;
            ioHandler.print("Round " + numDiceRolls + ": ");
            
            // Rolling for player one
            ioHandler.print("Rolling dice for player one ");
            dice.roll();
            ioHandler.print("Die 1 is " + dice.getValue1());
            ioHandler.print("Die 2 is " + dice.getValue2());
            player1.setTotalScore(dice.getSum());

            // Rolling for player two
            ioHandler.print("Rolling dice for player two ");
            dice.roll();
            ioHandler.print("Die 1 is " + dice.getValue1());
            ioHandler.print("Die 2 is " + dice.getValue2());
            player2.setTotalScore(dice.getSum());

            ioHandler.print(player1.toString());
            ioHandler.print(player2.toString());

            // Check for win/draw
            if (player1.getTotalScore() >= SCORE_TO_WIN && player2.getTotalScore() >= SCORE_TO_WIN) {
                ioHandler.print("DRAW");
                break;
            } else if (player1.getTotalScore() >= SCORE_TO_WIN) {
                ioHandler.print(player1.getName() + " wins");
                break;
            } else if (player2.getTotalScore() >= SCORE_TO_WIN) {
                ioHandler.print(player2.getName() + " wins");
                break;
            } else {
                ioHandler.print("No winner yet");
                choice = ioHandler.getLowercaseString("Roll the dice again? (y/n): ");
            }
        }
        ioHandler.print("Good bye!");
    }
}

