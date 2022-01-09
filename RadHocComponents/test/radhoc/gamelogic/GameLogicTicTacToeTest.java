package radhoc.gamelogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import radhoc.gamestates.*;
import radhoc.gamestates.GameStateTicTacToe.Shape;
import radhoc.mock.MockCommunication;
import radhoc.mock.MockUpdateListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("PointlessArithmeticExpression")
public class GameLogicTicTacToeTest {
	
	private MockCommunication mockCommunication;
	private GameStateManager gameStateManager;
	private GameLogicManager gameLogicManager;
	
	@BeforeEach
	void setUp() throws IOException {
		
		File directory = Files.createTempDirectory("radhoctests").toFile();
		
		mockCommunication = new MockCommunication();
		gameStateManager = GameStateManagerFactory.createGameStateManager(directory);
		gameLogicManager = GameLogicManagerFactory.createGameLogicManager(gameStateManager, mockCommunication);
		
	}
	
	@Test
	void playShapeAt() {
		
		GameStateTicTacToe gameState = (GameStateTicTacToe) gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Clara", 3, 5, true);
		GameLogicTicTacToe gameLogic = (GameLogicTicTacToe) gameLogicManager.getGameLogic(gameState);
		
		mockCommunication.assertNotMoved();
		boolean result = gameLogic.playShapeAt(2, 1);
		
		assertTrue(result);
		assertFalse(gameState.isPlayable());
		assertEquals(Shape.CROSS, gameState.getShapeAt(2, 1));
		
		mockCommunication.assertMoved(3, 5, new byte[] {2*3 + 1}); // x: 2; y:1
		
	}
	
	@Test
	void playShapeOutOfBounds() {
		
		GameStateTicTacToe gameState = (GameStateTicTacToe) gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Hans", 17, 8, true);
		GameLogicTicTacToe gameLogic = (GameLogicTicTacToe) gameLogicManager.getGameLogic(gameState);
		
		MockUpdateListener mockUpdateListener = new MockUpdateListener();
		gameState.setUpdateListener(mockUpdateListener);
		
		assertThrows(IllegalArgumentException.class, () -> gameLogic.playShapeAt(-1, 0));
		assertThrows(IllegalArgumentException.class, () -> gameLogic.playShapeAt(3, 2));
		assertThrows(IllegalArgumentException.class, () -> gameLogic.playShapeAt(1, -1));
		assertThrows(IllegalArgumentException.class, () -> gameLogic.playShapeAt(2, 3));
		
		mockUpdateListener.assertNotUpdated(); //Make sure GameState wasn't changed
		mockCommunication.assertNotMoved();
		
	}
	
	@Test
	void playShapeDuringOpponentTurn() {
		
		GameStateTicTacToe gameStateA = (GameStateTicTacToe) gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Alice", 1, 10, true);
		GameStateTicTacToe gameStateB = (GameStateTicTacToe) gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Bernd", 2, 20, false);
		GameLogicTicTacToe gameLogicA = (GameLogicTicTacToe) gameLogicManager.getGameLogic(gameStateA);
		GameLogicTicTacToe gameLogicB = (GameLogicTicTacToe) gameLogicManager.getGameLogic(gameStateB);
		
		MockUpdateListener mockUpdateListener = new MockUpdateListener();
		gameStateA.setUpdateListener(mockUpdateListener);
		gameStateB.setUpdateListener(mockUpdateListener);
		
		boolean result = gameLogicA.playShapeAt(1, 2);
		
		assertTrue(result);
		mockUpdateListener.assertUpdated();
		mockCommunication.assertMoved(1, 10, new byte[] {1*3 + 2});
		
		boolean resultA = gameLogicA.playShapeAt(1, 1);
		boolean resultB = gameLogicB.playShapeAt(1, 1);
		
		assertFalse(resultA);
		assertFalse(resultB);
		mockUpdateListener.assertNotUpdated();
		mockCommunication.assertNotMoved();
		
	}
	
	@Test
	void enemyPlaysShape() {
		
		GameStateTicTacToe gameStateA = (GameStateTicTacToe) gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Peter", 17, 8, true);
		GameStateTicTacToe gameStateB = (GameStateTicTacToe) gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Lara", 18, 10, false);
		GameLogicTicTacToe gameLogicA = (GameLogicTicTacToe) gameLogicManager.getGameLogic(gameStateA);
		GameLogicTicTacToe gameLogicB = (GameLogicTicTacToe) gameLogicManager.getGameLogic(gameStateB);
		
		MockUpdateListener mockUpdateListener = new MockUpdateListener();
		gameStateA.setUpdateListener(mockUpdateListener);
		gameStateB.setUpdateListener(mockUpdateListener);
		
		gameLogicA.playShapeAt(1, 1);
		mockUpdateListener.assertUpdated();
		
		mockCommunication.mockMove(8, new byte[] {1*3 + 2});
		mockUpdateListener.assertUpdated();
		mockCommunication.mockMove(10, new byte[] {2*3 + 0});
		mockUpdateListener.assertUpdated();
		
		assertEquals(Shape.CROSS, gameStateA.getShapeAt(1, 1));
		assertEquals(Shape.CIRCLE, gameStateA.getShapeAt(1, 2));
		assertEquals(Shape.CROSS, gameStateB.getShapeAt(2, 0));
		assertTrue(gameStateA.isPlayable());
		assertTrue(gameStateB.isPlayable());
		
	}
	
	@Test
	void playShapeOnOccupiedField() {
		
		GameStateTicTacToe gameStateA = (GameStateTicTacToe) gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Alice", 1, 10, true);
		GameStateTicTacToe gameStateB = (GameStateTicTacToe) gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Bernd", 2, 20, false);
		GameLogicTicTacToe gameLogicA = (GameLogicTicTacToe) gameLogicManager.getGameLogic(gameStateA);
		GameLogicTicTacToe gameLogicB = (GameLogicTicTacToe) gameLogicManager.getGameLogic(gameStateB);
		
		MockUpdateListener mockUpdateListener = new MockUpdateListener();
		gameStateA.setUpdateListener(mockUpdateListener);
		gameStateB.setUpdateListener(mockUpdateListener);
		
		boolean result = gameLogicA.playShapeAt(0, 0);
		assertTrue(result);
		mockUpdateListener.assertUpdated();
		mockCommunication.assertMoved(1, 10, new byte[] {0*3 + 0});
		
		mockCommunication.mockMove(10, new byte[] {1*3 + 1});
		mockUpdateListener.assertUpdated();
		mockCommunication.mockMove(20, new byte[] {2*3 + 2});
		mockUpdateListener.assertUpdated();
		
		boolean resultA1 = gameLogicA.playShapeAt(0, 0);
		boolean resultA2 = gameLogicA.playShapeAt(1, 1);
		boolean resultB = gameLogicB.playShapeAt(2, 2);
		
		assertFalse(resultA1);
		assertFalse(resultA2);
		assertFalse(resultB);
		mockUpdateListener.assertNotUpdated();
		mockCommunication.assertNotMoved();
		
	}
	
	@Test
	void winGame() {
		
		GameStateTicTacToe gameState = (GameStateTicTacToe) gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Marie", 15, 25, true);
		GameLogicTicTacToe gameLogic = (GameLogicTicTacToe) gameLogicManager.getGameLogic(gameState);
		
		gameLogic.playShapeAt(1, 1);
		mockCommunication.mockMove(25, new byte[] {0*3 + 1});
		gameLogic.playShapeAt(0, 2);
		mockCommunication.mockMove(25, new byte[] {2*3 + 0});
		gameLogic.playShapeAt(2, 2);
		mockCommunication.mockMove(25, new byte[] {1*3 + 2});
		
		assertEquals(GameResult.STILL_PLAYING, gameState.getResult());
		
		gameLogic.playShapeAt(0, 0);
		
		assertEquals(GameResult.VICTORY, gameState.getResult());
		
	}
	
	@Test
	void loseGame() {
		
		GameStateTicTacToe gameState = (GameStateTicTacToe) gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Maya", 18, 28, false);
		GameLogicTicTacToe gameLogic = (GameLogicTicTacToe) gameLogicManager.getGameLogic(gameState);
		
		mockCommunication.mockMove(28, new byte[] {0*3 + 1});
		gameLogic.playShapeAt(0, 2);
		mockCommunication.mockMove(28, new byte[] {1*3 + 1});
		gameLogic.playShapeAt(2, 0);
		
		assertEquals(GameResult.STILL_PLAYING, gameState.getResult());
		
		mockCommunication.mockMove(28, new byte[] {2*3 + 1});
		
		assertEquals(GameResult.DEFEAT, gameState.getResult());
		
	}
	
	@Test
	void drawGame() {
		
		GameStateTicTacToe gameState = (GameStateTicTacToe) gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Albert", 20, 30, true);
		GameLogicTicTacToe gameLogic = (GameLogicTicTacToe) gameLogicManager.getGameLogic(gameState);
		
		gameLogic.playShapeAt(0, 1);
		mockCommunication.mockMove(30, new byte[] {1*3 + 1});
		gameLogic.playShapeAt(0, 2);
		mockCommunication.mockMove(30, new byte[] {0*3 + 0});
		gameLogic.playShapeAt(2, 2);
		mockCommunication.mockMove(30, new byte[] {1*3 + 2});
		gameLogic.playShapeAt(1, 0);
		mockCommunication.mockMove(30, new byte[] {2*3 + 1});
		
		assertEquals(GameResult.STILL_PLAYING, gameState.getResult());
		
		gameLogic.playShapeAt(2, 0);
		
		assertEquals(GameResult.DRAW, gameState.getResult());
		
	}
	
}