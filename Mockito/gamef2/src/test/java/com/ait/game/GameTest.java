package com.ait.game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.mockito.Mockito.*;
import org.mockito.InOrder;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class GameTest {
    private Game game;
    private IOHandler mockIOHandler;
    private PairOfDice mockDice;

    @BeforeEach
    public void setUp() {
        mockIOHandler = mock(IOHandler.class);
        mockDice = mock(PairOfDice.class);
        game = new Game(mockIOHandler, mockDice);
    }

    @Test
    public void testTerminateGameBeforeEnd() {
        // Mocking input and dice behavior
        when(mockIOHandler.getString(anyString())).thenReturn("Player1", "Player2");
        when(mockIOHandler.getLowercaseString(anyString())).thenReturn("y", "n");

        // Mocking dice rolls
        when(mockDice.getValue1()).thenReturn(5);
        when(mockDice.getValue2()).thenReturn(6);
        when(mockDice.getSum()).thenReturn(11);

        // Run the game
        game.start();

        // Verify the sequence of interactions with IOHandler
        InOrder inOrder = inOrder(mockIOHandler);

        // Verifying the exact order of method calls
        inOrder.verify(mockIOHandler).print("Welcome to the Dice Roller application\n");
        inOrder.verify(mockIOHandler).getString("Please enter player one name: ");
        inOrder.verify(mockIOHandler).getString("Please enter player two name: ");
        inOrder.verify(mockIOHandler).getLowercaseString("Roll the dice? (y/n): ");

        // Verifying rolling for player one
        inOrder.verify(mockIOHandler).print("Round 1: ");
        inOrder.verify(mockIOHandler).print("Rolling dice for player one ");
        inOrder.verify(mockIOHandler).print("Die 1 is 5");
        inOrder.verify(mockIOHandler).print("Die 2 is 6");

        // Verifying rolling for player two
        inOrder.verify(mockIOHandler).print("Rolling dice for player two ");
        inOrder.verify(mockIOHandler).print("Die 1 is 5");
        inOrder.verify(mockIOHandler).print("Die 2 is 6");

        // Verify print statements for players' scores
        inOrder.verify(mockIOHandler).print("Current score for Player1 is 11");
        inOrder.verify(mockIOHandler).print("Current score for Player2 is 11");

        // Verify no winner yet
        inOrder.verify(mockIOHandler).print("No winner yet");

        // Verify the goodbye message
        inOrder.verify(mockIOHandler).print("Good bye!");

        // Verify that no more interactions happen after this
        inOrder.verifyNoMoreInteractions();
    }
    
    @Test
    public void testPlayerOneWins() {
        // Mocking input and dice behavior
        when(mockIOHandler.getString(anyString())).thenReturn("Player1", "Player2");
        when(mockIOHandler.getLowercaseString(anyString())).thenReturn("y", "y");

        // Mocking dice rolls
        when(mockDice.getValue1()).thenReturn(6,1,6,1);
        when(mockDice.getValue2()).thenReturn(6,3,6,2);
        when(mockDice.getSum()).thenReturn(12,4,12,3);

        // Run the game
        game.start();

        // Verify the sequence of interactions with IOHandler
        InOrder inOrder = inOrder(mockIOHandler);

        // Verifying the exact order of method calls
        inOrder.verify(mockIOHandler).print("Welcome to the Dice Roller application\n");
        inOrder.verify(mockIOHandler).getString("Please enter player one name: ");
        inOrder.verify(mockIOHandler).getString("Please enter player two name: ");
        inOrder.verify(mockIOHandler).getLowercaseString("Roll the dice? (y/n): ");

        // Verifying rolling for player one
        inOrder.verify(mockIOHandler).print("Round 1: ");
        inOrder.verify(mockIOHandler).print("Rolling dice for player one ");
        inOrder.verify(mockIOHandler).print("Die 1 is 6");
        inOrder.verify(mockIOHandler).print("Die 2 is 6");

        // Verifying rolling for player two
        inOrder.verify(mockIOHandler).print("Rolling dice for player two ");
        inOrder.verify(mockIOHandler).print("Die 1 is 1");
        inOrder.verify(mockIOHandler).print("Die 2 is 3");

        // Verify print statements for players' scores
        inOrder.verify(mockIOHandler).print("Current score for Player1 is 12");
        inOrder.verify(mockIOHandler).print("Current score for Player2 is 4");

        // Verify no winner yet
        inOrder.verify(mockIOHandler).print("No winner yet");
        
        inOrder.verify(mockIOHandler).getLowercaseString("Roll the dice again? (y/n): ");

        // Verifying rolling for player one
        inOrder.verify(mockIOHandler).print("Round 2: ");
        inOrder.verify(mockIOHandler).print("Rolling dice for player one ");
        inOrder.verify(mockIOHandler).print("Die 1 is 6");
        inOrder.verify(mockIOHandler).print("Die 2 is 6");

        // Verifying rolling for player two
        inOrder.verify(mockIOHandler).print("Rolling dice for player two ");
        inOrder.verify(mockIOHandler).print("Die 1 is 1");
        inOrder.verify(mockIOHandler).print("Die 2 is 2");

        // Verify print statements for players' scores
        inOrder.verify(mockIOHandler).print("Current score for Player1 is 24");
        inOrder.verify(mockIOHandler).print("Current score for Player2 is 7");

        // Verify no winner yet
        inOrder.verify(mockIOHandler).print("Player1 wins");

        // Verify the goodbye message
        inOrder.verify(mockIOHandler).print("Good bye!");

        // Verify that no more interactions happen after this
        inOrder.verifyNoMoreInteractions();
    }
    
    @Test
    public void testPlayerTwoWins() {
        // Mocking input and dice behavior
        when(mockIOHandler.getString(anyString())).thenReturn("Player1", "Player2");
        when(mockIOHandler.getLowercaseString(anyString())).thenReturn("y", "y");

        // Mocking dice rolls
        when(mockDice.getValue1()).thenReturn(1,6,1,6);
        when(mockDice.getValue2()).thenReturn(3,6,2,6);
        when(mockDice.getSum()).thenReturn(4,12,3,12);

        // Run the game
        game.start();

        // Verify the sequence of interactions with IOHandler
        InOrder inOrder = inOrder(mockIOHandler);

        // Verifying the exact order of method calls
        inOrder.verify(mockIOHandler).print("Welcome to the Dice Roller application\n");
        inOrder.verify(mockIOHandler).getString("Please enter player one name: ");
        inOrder.verify(mockIOHandler).getString("Please enter player two name: ");
        inOrder.verify(mockIOHandler).getLowercaseString("Roll the dice? (y/n): ");

        // Verifying rolling for player one
        inOrder.verify(mockIOHandler).print("Round 1: ");
        inOrder.verify(mockIOHandler).print("Rolling dice for player one ");
        inOrder.verify(mockIOHandler).print("Die 1 is 1");
        inOrder.verify(mockIOHandler).print("Die 2 is 3");

        // Verifying rolling for player two
        inOrder.verify(mockIOHandler).print("Rolling dice for player two ");
        inOrder.verify(mockIOHandler).print("Die 1 is 6");
        inOrder.verify(mockIOHandler).print("Die 2 is 6");

        // Verify print statements for players' scores
        inOrder.verify(mockIOHandler).print("Current score for Player1 is 4");
        inOrder.verify(mockIOHandler).print("Current score for Player2 is 12");

        // Verify no winner yet
        inOrder.verify(mockIOHandler).print("No winner yet");
        
        inOrder.verify(mockIOHandler).getLowercaseString("Roll the dice again? (y/n): ");

        // Verifying rolling for player one
        inOrder.verify(mockIOHandler).print("Round 2: ");
        inOrder.verify(mockIOHandler).print("Rolling dice for player one ");
        inOrder.verify(mockIOHandler).print("Die 1 is 1");
        inOrder.verify(mockIOHandler).print("Die 2 is 2");

        // Verifying rolling for player two
        inOrder.verify(mockIOHandler).print("Rolling dice for player two ");
        inOrder.verify(mockIOHandler).print("Die 1 is 6");
        inOrder.verify(mockIOHandler).print("Die 2 is 6");

        // Verify print statements for players' scores
        inOrder.verify(mockIOHandler).print("Current score for Player1 is 7");
        inOrder.verify(mockIOHandler).print("Current score for Player2 is 24");

        // Verify no winner yet
        inOrder.verify(mockIOHandler).print("Player2 wins");

        // Verify the goodbye message
        inOrder.verify(mockIOHandler).print("Good bye!");

        // Verify that no more interactions happen after this
        inOrder.verifyNoMoreInteractions();
    }
    
    @Test
    public void testADraw() {
        // Mocking input and dice behavior
        when(mockIOHandler.getString(anyString())).thenReturn("Player1", "Player2");
        when(mockIOHandler.getLowercaseString(anyString())).thenReturn("y", "y");

        // Mocking dice rolls
        when(mockDice.getValue1()).thenReturn(6,6,5,5);
        when(mockDice.getValue2()).thenReturn(6,6,5,5);
        when(mockDice.getSum()).thenReturn(12,12,10,10);

        // Run the game
        game.start();

        // Verify the sequence of interactions with IOHandler
        InOrder inOrder = inOrder(mockIOHandler);

        // Verifying the exact order of method calls
        inOrder.verify(mockIOHandler).print("Welcome to the Dice Roller application\n");
        inOrder.verify(mockIOHandler).getString("Please enter player one name: ");
        inOrder.verify(mockIOHandler).getString("Please enter player two name: ");
        inOrder.verify(mockIOHandler).getLowercaseString("Roll the dice? (y/n): ");

        // Verifying rolling for player one
        inOrder.verify(mockIOHandler).print("Round 1: ");
        inOrder.verify(mockIOHandler).print("Rolling dice for player one ");
        inOrder.verify(mockIOHandler).print("Die 1 is 6");
        inOrder.verify(mockIOHandler).print("Die 2 is 6");

        // Verifying rolling for player two
        inOrder.verify(mockIOHandler).print("Rolling dice for player two ");
        inOrder.verify(mockIOHandler).print("Die 1 is 6");
        inOrder.verify(mockIOHandler).print("Die 2 is 6");

        // Verify print statements for players' scores
        inOrder.verify(mockIOHandler).print("Current score for Player1 is 12");
        inOrder.verify(mockIOHandler).print("Current score for Player2 is 12");

        // Verify no winner yet
        inOrder.verify(mockIOHandler).print("No winner yet");
        
        inOrder.verify(mockIOHandler).getLowercaseString("Roll the dice again? (y/n): ");

        // Verifying rolling for player one
        inOrder.verify(mockIOHandler).print("Round 2: ");
        inOrder.verify(mockIOHandler).print("Rolling dice for player one ");
        inOrder.verify(mockIOHandler).print("Die 1 is 5");
        inOrder.verify(mockIOHandler).print("Die 2 is 5");

        // Verifying rolling for player two
        inOrder.verify(mockIOHandler).print("Rolling dice for player two ");
        inOrder.verify(mockIOHandler).print("Die 1 is 5");
        inOrder.verify(mockIOHandler).print("Die 2 is 5");

        // Verify print statements for players' scores
        inOrder.verify(mockIOHandler).print("Current score for Player1 is 22");
        inOrder.verify(mockIOHandler).print("Current score for Player2 is 22");

        // Verify no winner yet
        inOrder.verify(mockIOHandler).print("DRAW");

        // Verify the goodbye message
        inOrder.verify(mockIOHandler).print("Good bye!");

        // Verify that no more interactions happen after this
        inOrder.verifyNoMoreInteractions();
    }
}
