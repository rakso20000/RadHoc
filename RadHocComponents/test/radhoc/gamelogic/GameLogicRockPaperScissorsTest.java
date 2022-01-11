package radhoc.gamelogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import radhoc.gamestates.*;
import radhoc.gamestates.GameStateRockPaperScissors.Shape;
import radhoc.mock.MockCommunication;
import radhoc.mock.MockUpdateListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicRockPaperScissorsTest {
	
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
	void playShape() {
		
		GameStateRockPaperScissors gameState = (GameStateRockPaperScissors) gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Alice", 1, 10, true);
		GameLogicRockPaperScissors gameLogic = (GameLogicRockPaperScissors) gameLogicManager.getGameLogic(gameState);
		
		mockCommunication.assertNotMoved();
		
		boolean result = gameLogic.playShape(Shape.ROCK);
		assertTrue(result);
		assertArrayEquals(new Shape[] {Shape.ROCK}, gameState.getPlayerShapes().toArray());
		assertTrue(gameState.getOpponentShapes().isEmpty());
		mockCommunication.assertMoved(1, 10, new byte[] {0}); //0: ROCK, 1: PAPER, 2: SCISSORS
		
	}
	
	@Test
	void enemyPlaysShape() {
		
		GameStateRockPaperScissors gameState = (GameStateRockPaperScissors) gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Bernd", 2, 20, false);
		GameLogicRockPaperScissors gameLogic = (GameLogicRockPaperScissors) gameLogicManager.getGameLogic(gameState);
		
		mockCommunication.mockMove(20, new byte[] {1});
		
		assertTrue(gameState.getPlayerShapes().isEmpty());
		assertArrayEquals(new Shape[] {Shape.PAPER}, gameState.getOpponentShapes().toArray());
		
	}
	
	@Test
	void playerScores() {
		
		GameStateRockPaperScissors gameState = (GameStateRockPaperScissors) gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Clara", 3, 30, false);
		GameLogicRockPaperScissors gameLogic = (GameLogicRockPaperScissors) gameLogicManager.getGameLogic(gameState);
		
		boolean result = gameLogic.playShape(Shape.SCISSORS);
		mockCommunication.mockMove(30, new byte[] {1});
		
		assertTrue(result);
		assertEquals(1, gameState.getPlayerScore());
		assertEquals(0, gameState.getOpponentScore());
		mockCommunication.assertMoved(3, 30, new byte[] {2});
		
		mockCommunication.mockMove(30, new byte[] {0});
		result = gameLogic.playShape(Shape.PAPER);
		
		assertTrue(result);
		assertEquals(2, gameState.getPlayerScore());
		assertEquals(0, gameState.getOpponentScore());
		mockCommunication.assertMoved(3, 30, new byte[] {1});
		
		//need new GameState because game is already over
		
		gameState = (GameStateRockPaperScissors) gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Clara", 3, 31, false);
		gameLogic = (GameLogicRockPaperScissors) gameLogicManager.getGameLogic(gameState);
		
		mockCommunication.mockMove(31, new byte[] {2});
		result = gameLogic.playShape(Shape.ROCK);
		
		assertTrue(result);
		assertEquals(1, gameState.getPlayerScore());
		assertEquals(0, gameState.getOpponentScore());
		mockCommunication.assertMoved(3, 31, new byte[] {0});
		
	}
	
	@Test
	void opponentScored() {
		
		GameStateRockPaperScissors gameState = (GameStateRockPaperScissors) gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Dieter", 4, 40, false);
		GameLogicRockPaperScissors gameLogic = (GameLogicRockPaperScissors) gameLogicManager.getGameLogic(gameState);
		
		mockCommunication.mockMove(40, new byte[] {1});
		boolean result = gameLogic.playShape(Shape.ROCK);
		
		assertTrue(result);
		assertEquals(0, gameState.getPlayerScore());
		assertEquals(1, gameState.getOpponentScore());
		mockCommunication.assertMoved(4, 40, new byte[] {0});
		
		result = gameLogic.playShape(Shape.PAPER);
		mockCommunication.mockMove(40, new byte[] {2});
		
		assertTrue(result);
		assertEquals(0, gameState.getPlayerScore());
		assertEquals(2, gameState.getOpponentScore());
		mockCommunication.assertMoved(4, 40, new byte[] {1});
		
		//need new GameState because game is already over
		
		gameState = (GameStateRockPaperScissors) gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Dieter", 4, 41, false);
		gameLogic = (GameLogicRockPaperScissors) gameLogicManager.getGameLogic(gameState);
		
		mockCommunication.mockMove(41, new byte[] {0});
		result = gameLogic.playShape(Shape.SCISSORS);
		
		assertTrue(result);
		assertEquals(0, gameState.getPlayerScore());
		assertEquals(1, gameState.getOpponentScore());
		mockCommunication.assertMoved(4, 41, new byte[] {2});
		
	}
	
	@Test
	void neitherScored() {
		
		GameStateRockPaperScissors gameState = (GameStateRockPaperScissors) gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Gustav", 5, 50, true);
		GameLogicRockPaperScissors gameLogic = (GameLogicRockPaperScissors) gameLogicManager.getGameLogic(gameState);
		
		boolean result = gameLogic.playShape(Shape.ROCK);
		mockCommunication.mockMove(50, new byte[] {0});
		
		assertTrue(result);
		assertEquals(0, gameState.getPlayerScore());
		assertEquals(0, gameState.getOpponentScore());
		mockCommunication.assertMoved(5, 50, new byte[] {0});
		
		mockCommunication.mockMove(50, new byte[] {1});
		result = gameLogic.playShape(Shape.PAPER);
		
		assertTrue(result);
		assertEquals(0, gameState.getPlayerScore());
		assertEquals(0, gameState.getOpponentScore());
		mockCommunication.assertMoved(5, 50, new byte[] {1});
		
		result = gameLogic.playShape(Shape.SCISSORS);
		mockCommunication.mockMove(50, new byte[] {2});
		
		assertTrue(result);
		assertEquals(0, gameState.getPlayerScore());
		assertEquals(0, gameState.getOpponentScore());
		mockCommunication.assertMoved(5, 50, new byte[] {2});
		
	}
	
	@Test
	void playShapeDuringOpponentTurn() {
		
		GameStateRockPaperScissors gameState = (GameStateRockPaperScissors) gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Karl", 6, 60, true);
		GameLogicRockPaperScissors gameLogic = (GameLogicRockPaperScissors) gameLogicManager.getGameLogic(gameState);
		
		MockUpdateListener mockUpdateListener = new MockUpdateListener();
		gameState.setUpdateListener(mockUpdateListener);
		
		assertTrue(gameState.isPlayable());
		boolean result = gameLogic.playShape(Shape.ROCK);
		assertTrue(result);
		mockCommunication.assertMoved(6, 60, new byte[] {0});
		mockUpdateListener.assertUpdated();
		
		assertFalse(gameState.isPlayable());
		result = gameLogic.playShape(Shape.PAPER);
		assertFalse(result);
		mockCommunication.assertNotMoved();
		mockUpdateListener.assertNotUpdated();
		
		mockCommunication.mockMove(60, new byte[] {0});
		
		assertTrue(gameState.isPlayable());
		result = gameLogic.playShape(Shape.SCISSORS);
		assertTrue(result);
		mockCommunication.assertMoved(6, 60, new byte[] {2});
		mockUpdateListener.assertUpdated();
		
		assertFalse(gameState.isPlayable());
		result = gameLogic.playShape(Shape.ROCK);
		assertFalse(result);
		mockCommunication.assertNotMoved();
		mockUpdateListener.assertNotUpdated();
		
		mockCommunication.mockMove(60, new byte[] {1});
		
		assertTrue(gameState.isPlayable());
		result = gameLogic.playShape(Shape.PAPER);
		assertTrue(result);
		mockCommunication.assertMoved(6, 60, new byte[] {1});
		mockUpdateListener.assertUpdated();
		
		gameState = (GameStateRockPaperScissors) gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Karl", 6, 61, false);
		gameLogic = (GameLogicRockPaperScissors) gameLogicManager.getGameLogic(gameState);
		
		gameState.setUpdateListener(mockUpdateListener);
		
		assertTrue(gameState.isPlayable());
		result = gameLogic.playShape(Shape.ROCK);
		assertTrue(result);
		mockCommunication.assertMoved(6, 61, new byte[] {0});
		mockUpdateListener.assertUpdated();
		
		assertFalse(gameState.isPlayable());
		result = gameLogic.playShape(Shape.PAPER);
		assertFalse(result);
		mockCommunication.assertNotMoved();
		mockUpdateListener.assertNotUpdated();
		
		mockCommunication.mockMove(61, new byte[] {0});
		mockCommunication.mockMove(61, new byte[] {1});
		
		assertTrue(gameState.isPlayable());
		result = gameLogic.playShape(Shape.SCISSORS);
		assertTrue(result);
		mockCommunication.assertMoved(6, 61, new byte[] {2});
		mockUpdateListener.assertUpdated();
		
		assertTrue(gameState.isPlayable());
		result = gameLogic.playShape(Shape.ROCK);
		assertTrue(result);
		mockCommunication.assertMoved(6, 61, new byte[] {0});
		mockUpdateListener.assertUpdated();
		
		assertFalse(gameState.isPlayable());
		result = gameLogic.playShape(Shape.PAPER);
		assertFalse(result);
		mockCommunication.assertNotMoved();
		mockUpdateListener.assertNotUpdated();
		
	}
	
	@Test
	void winGame() {
		
		GameStateRockPaperScissors gameState = (GameStateRockPaperScissors) gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Tom", 7, 70, true);
		GameLogicRockPaperScissors gameLogic = (GameLogicRockPaperScissors) gameLogicManager.getGameLogic(gameState);
		
		gameLogic.playShape(Shape.ROCK);
		mockCommunication.mockMove(70, new byte[] {2});
		mockCommunication.mockMove(70, new byte[] {1});
		gameLogic.playShape(Shape.ROCK);
		mockCommunication.mockMove(70, new byte[] {1});
		
		assertTrue(gameState.isPlayable());
		assertEquals(GameResult.STILL_PLAYING, gameState.getResult());
		
		gameLogic.playShape(Shape.SCISSORS);
		
		assertFalse(gameState.isPlayable());
		assertEquals(GameResult.VICTORY, gameState.getResult());
		
	}
	
	@Test
	void loseGame() {
		
		GameStateRockPaperScissors gameState = (GameStateRockPaperScissors) gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Tom", 7, 71, true);
		GameLogicRockPaperScissors gameLogic = (GameLogicRockPaperScissors) gameLogicManager.getGameLogic(gameState);
		
		gameLogic.playShape(Shape.SCISSORS);
		mockCommunication.mockMove(71, new byte[] {0});
		gameLogic.playShape(Shape.PAPER);
		
		assertFalse(gameState.isPlayable());
		assertEquals(GameResult.STILL_PLAYING, gameState.getResult());
		
		mockCommunication.mockMove(71, new byte[] {2});
		
		assertFalse(gameState.isPlayable());
		assertEquals(GameResult.DEFEAT, gameState.getResult());
		
	}
	
	@Test
	void drawGame() {
		
		GameStateRockPaperScissors gameState = (GameStateRockPaperScissors) gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Tom", 7, 72, true);
		GameLogicRockPaperScissors gameLogic = (GameLogicRockPaperScissors) gameLogicManager.getGameLogic(gameState);
		
		gameLogic.playShape(Shape.PAPER);
		mockCommunication.mockMove(72, new byte[] {1});
		gameLogic.playShape(Shape.ROCK);
		mockCommunication.mockMove(72, new byte[] {2});
		mockCommunication.mockMove(72, new byte[] {2});
		
		assertTrue(gameState.isPlayable());
		assertEquals(GameResult.STILL_PLAYING, gameState.getResult());
		
		gameLogic.playShape(Shape.PAPER);
		
		assertFalse(gameState.isPlayable());
		assertEquals(GameResult.DRAW, gameState.getResult());
		
	}
	
}