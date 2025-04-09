package com.iarpg.app;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.iarpg.app.data.GameBoard;
import com.iarpg.app.data.Position;
import com.iarpg.app.data.Room;

public class GameBoardTest {
//    private GameBoard gameBoard;
//    private final int height = 5;
//    private final int width = 5;
//
//    @Before
//    public void setUp() {
//        gameBoard = new GameBoard(height, width);
//    }
//
//    @Test
//    public void testBoardInitialization() {
//        assertNotNull(gameBoard);
//
//        // Vérification des dimensions de la grille
//        assertEquals(height, gameBoard.getGrid().size());
//        for (int y = 0; y < height; y++) {
//            assertEquals(width, gameBoard.getGrid().get(y).size());
//            for (int x = 0; x < width; x++) {
//                assertNotNull(gameBoard.getGrid().get(y).get(x));
//            }
//        }
//    }
//
//    @Test
//    public void testMovePlayer() {
//        Position initialPosition = new Position(gameBoard.getCurrentPosition().getX(), gameBoard.getCurrentPosition().getY());
//
//        gameBoard.movePlayer("left");
//        assertEquals(initialPosition.getX() - 1, gameBoard.getCurrentPosition().getX());
//        assertEquals(initialPosition.getY(), gameBoard.getCurrentPosition().getY());
//
//        gameBoard.movePlayer("right");
//        assertEquals(initialPosition.getX(), gameBoard.getCurrentPosition().getX());
//        assertEquals(initialPosition.getY(), gameBoard.getCurrentPosition().getY());
//
//        gameBoard.movePlayer("up");
//        assertEquals(initialPosition.getX(), gameBoard.getCurrentPosition().getX());
//        assertEquals(initialPosition.getY() - 1, gameBoard.getCurrentPosition().getY());
//
//        gameBoard.movePlayer("down");
//        assertEquals(initialPosition.getX(), gameBoard.getCurrentPosition().getX());
//        assertEquals(initialPosition.getY(), gameBoard.getCurrentPosition().getY());
//    }
//
//    @Test
//    public void testStartPositionIsWithinBounds() {
//        Position startPosition = gameBoard.getStartPosition();
//        assertTrue(startPosition.getX() >= 0 && startPosition.getX() < width);
//        assertEquals(height - 1, startPosition.getY());
//    }
//
//    @Test
//    public void testExitPositionIsWithinBounds() {
//        Position exitPosition = gameBoard.getExitPosition();
//        assertTrue(exitPosition.getX() >= 0 && exitPosition.getX() < width);
//        assertEquals(0, exitPosition.getY());
//    }
}
