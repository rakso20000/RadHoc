package radhoc.gamestates;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import radhoc.gamestates.impl.GameStateManagerImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateManagerTest {
	
	private GameStateManager gsm;
	
	@BeforeEach
	void setUp() throws IOException {
		
		Path directory = Files.createTempDirectory("radhoctests");
		
		gsm = GameStateManagerFactory.createGameStateManager(directory);
		
	}
	
	@Test
	void getEmptyGameStates() {
		
		List<GameState> gameStates = gsm.getAllGameStates();
		assertTrue(gameStates.isEmpty());
		
	}
	
	@Test
	void createGameState() {
		gsm.createGameState(GameType.TIC_TAC_TOE, "Dieter", 420, 1337);
		
		List<GameState> gameStates = gsm.getAllGameStates();
		assertEquals(1, gameStates.size());
		
		GameState gs = gameStates.get(0);
		assertEquals(GameType.TIC_TAC_TOE, gs.getGameType());
		assertEquals("Dieter", gs.getOpponentName());
		assertEquals(420, gs.getOpponentID());
		assertEquals(1337, gs.getID());
	}
	
	@Test
	void findGameState() {
		
		gsm.createGameState(GameType.TIC_TAC_TOE, "Lara", 3, 5);
		gsm.createGameState(GameType.TIC_TAC_TOE, "Hans", 7, 8);
		
		GameState gsLara = gsm.getGameState(5);
		assertEquals("Lara", gsLara.getOpponentName());
		assertEquals(3, gsLara.getOpponentID());
		assertEquals(5, gsLara.getID());
		
		GameState gsHans = gsm.getGameState(8);
		assertEquals("Hans", gsHans.getOpponentName());
		assertEquals(7, gsHans.getOpponentID());
		assertEquals(8, gsHans.getID());
		
		assertThrows(IllegalArgumentException.class, () -> {
			gsm.getGameState(126);
		});
		
	}
	
}