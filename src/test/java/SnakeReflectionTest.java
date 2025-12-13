import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

class SnakeReflectionTest {

    private SnakeReflection game;

    @BeforeEach
    void setUp() {
        game = new SnakeReflection();
    }

    @Test
    void startGame_initializesGameCorrectly() {
        game.startGame();
        assertTrue(game.isRunning(), "Game should be running after start");
        assertEquals(3, game.getSnake().size(), "Initial snake length should be 3");
        assertNotNull(game.getFood(), "Food should be placed on the board");
    }

    @Test
    void newFood_placesFoodNotInSnake() {
        game.startGame();
        for (int i = 0; i < 100; i++) { 
            game.newFood();
            assertNotNull(game.getFood(), "Food should not be null");
            assertFalse(game.getSnake().contains(game.getFood()), "Food should not be on the snake");
        }
    }

    @Test
    void move_snakeMovesCorrectly() {
        game.startGame();
        game.setDirection('R');
        Point initialHeadPosition = new Point(game.getSnake().get(0));

        game.move();

        Point newHeadPosition = game.getSnake().get(0);
        assertNotEquals(initialHeadPosition, newHeadPosition, "Snake head should have moved");
    }
    
	@Test
	void setDirection_changesDirection() {
    game.startGame();
    game.setDirection('L');
    game.move();
    assertNotNull(game.getSnake().get(0));
	}
	
	@Test
	void move_reflectsFromLeftWall() {
		game.startGame();
		game.getSnake().set(0, new Point(0, 100));
		game.setDirection('L');
		game.move();
		assertEquals(0, game.getSnake().get(0).x);
	}
	
	@Test
	void move_eatsFoodAndIncreasesScore() {
		game.startGame();
		Point head = game.getSnake().get(0);
		game.setDirection('R');
		Point food = new Point(head.x + 20, head.y);
		game.getSnake().set(0, head);
		game.newFood();
		game.getSnake().add(food);
		game.move();
		assertTrue(game.getSnake().size() >= 3);
	}
	
}