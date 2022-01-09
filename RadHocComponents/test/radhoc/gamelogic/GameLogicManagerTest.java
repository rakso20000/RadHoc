package radhoc.gamelogic;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameStateManagerFactory;
import radhoc.gamestates.GameType;
import radhoc.mock.MockCommunication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameLogicManagerTest {
	
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
	void getGameLogicTest() {
		
		assertEquals(GameType.TIC_TAC_TOE, gameLogicManager.getGameLogic(gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Dieter", 123, 321, true)).getGameType());
		
	}
	
}