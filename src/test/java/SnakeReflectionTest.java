import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

class SnakeReflectionTest {

    private SnakeReflection game;

    @BeforeEach
    void setUp() {
        // Swing components require to be run on the Event Dispatch Thread.
        // For testing logic, we can often instantiate the class directly.
        game = new SnakeReflection();
    }

    @Test
    void startGame_initializesGameCorrectly() {
        game.startGame();
        assertTrue(game.isRunning(), "Game should be running after start");
        assertEquals(3, game.getSnake().size(), "Initial snake length should be 3");
        assertNotNull(game.getFood(), "Food should be placed on the board");
    }

    // Need to make some fields/methods public or package-private for testing,
    // or use reflection. Let's try to make them package-private for now.
    // To do that, I'll need to modify the SnakeReflection.java file.
    // For now, I will assume they are accessible for writing the test structure.

    @Test
    void newFood_placesFoodNotInSnake() {
        game.startGame();
        for (int i = 0; i < 100; i++) { // Run multiple times to increase confidence
            game.newFood();
            assertNotNull(game.getFood(), "Food should not be null");
            assertFalse(game.getSnake().contains(game.getFood()), "Food should not be on the snake");
        }
    }

    @Test
    void move_snakeMovesCorrectly() {
        game.startGame();
        // Set a known direction
        game.setDirection('R');
        Point initialHeadPosition = new Point(game.getSnake().get(0));

        game.move();

        Point newHeadPosition = game.getSnake().get(0);
        assertNotEquals(initialHeadPosition, newHeadPosition, "Snake head should have moved");
    }
    
}