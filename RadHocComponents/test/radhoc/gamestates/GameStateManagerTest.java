package radhoc.gamestates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import radhoc.mock.MockUpdateListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateManagerTest {
	
	private File directory;
	private GameStateManager gameStateManager;
	
	@BeforeEach
	void setUp() throws IOException {
		
		directory = Files.createTempDirectory("radhoctests").toFile();
		
		gameStateManager = GameStateManagerFactory.createGameStateManager(directory);
		
	}
	
	@Test
	void getEmptyGameStates() {
		
		List<GameState> gameStates = gameStateManager.getAllGameStates();
		assertTrue(gameStates.isEmpty());
		
	}
	
	@Test
	void createGameState() {
		
		gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Dieter", 420, 1337, true);
		
		List<GameState> gameStates = gameStateManager.getAllGameStates();
		assertEquals(1, gameStates.size());
		
		GameState gameState = gameStates.get(0);
		assertEquals(GameType.ROCK_PAPER_SCISSORS, gameState.getGameType());
		assertEquals("Dieter", gameState.getOpponentName());
		assertEquals(420, gameState.getOpponentID());
		assertEquals(1337, gameState.getID());
		
	}
	
	@Test
	void findGameState() {
		
		gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Lara", 3, 5, true);
		gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Hans", 7, 8, false);
		
		GameState gameStateLara = gameStateManager.getGameState(5);
		assertEquals(GameType.TIC_TAC_TOE, gameStateLara.getGameType());
		assertEquals("Lara", gameStateLara.getOpponentName());
		assertEquals(3, gameStateLara.getOpponentID());
		assertEquals(5, gameStateLara.getID());
		
		GameState gameStateHans = gameStateManager.getGameState(8);
		assertEquals(GameType.ROCK_PAPER_SCISSORS, gameStateHans.getGameType());
		assertEquals("Hans", gameStateHans.getOpponentName());
		assertEquals(7, gameStateHans.getOpponentID());
		assertEquals(8, gameStateHans.getID());
		
		assertThrows(IllegalArgumentException.class, () -> gameStateManager.getGameState(126));
		
	}
	
	@Test
	void saveGameStates() {
		
		gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Sara", 1, 2, true);
		gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Peter", 3, 4, false);
		
		gameStateManager.save();
		gameStateManager = GameStateManagerFactory.createGameStateManager(directory);
		
		GameState gameStateSara = gameStateManager.getGameState(2);
		assertEquals(GameType.ROCK_PAPER_SCISSORS, gameStateSara.getGameType());
		assertEquals("Sara", gameStateSara.getOpponentName());
		assertEquals(1, gameStateSara.getOpponentID());
		assertEquals(2, gameStateSara.getID());
		
		GameState gameStatePeter = gameStateManager.getGameState(4);
		assertEquals(GameType.TIC_TAC_TOE, gameStatePeter.getGameType());
		assertEquals("Peter", gameStatePeter.getOpponentName());
		assertEquals(3, gameStatePeter.getOpponentID());
		assertEquals(4, gameStatePeter.getID());
		
		gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Clara", 5, 6, false);
		
		gameStateManager.save();
		gameStateManager = GameStateManagerFactory.createGameStateManager(directory);
		
		gameStateSara = gameStateManager.getGameState(2);
		assertEquals(GameType.ROCK_PAPER_SCISSORS, gameStateSara.getGameType());
		assertEquals("Sara", gameStateSara.getOpponentName());
		assertEquals(1, gameStateSara.getOpponentID());
		assertEquals(2, gameStateSara.getID());
		
		gameStatePeter = gameStateManager.getGameState(4);
		assertEquals(GameType.TIC_TAC_TOE, gameStatePeter.getGameType());
		assertEquals("Peter", gameStatePeter.getOpponentName());
		assertEquals(3, gameStatePeter.getOpponentID());
		assertEquals(4, gameStatePeter.getID());
		
		GameState gameStateClara = gameStateManager.getGameState(6);
		assertEquals(GameType.ROCK_PAPER_SCISSORS, gameStateClara.getGameType());
		assertEquals("Clara", gameStateClara.getOpponentName());
		assertEquals(5, gameStateClara.getOpponentID());
		assertEquals(6, gameStateClara.getID());
		
	}
	
	@Test
	void updateListenerCalled() {
		
		MockUpdateListener listener = new MockUpdateListener();
		gameStateManager.setUpdateListener(listener);
		
		listener.assertNotUpdated();
		
		gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Alice", 1, 10, true);
		
		listener.assertUpdated();
		
		gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Bernd", 2, 200, false);
		
		listener.assertUpdated();
		
	}
	
}