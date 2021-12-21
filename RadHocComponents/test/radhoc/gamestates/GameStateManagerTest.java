package radhoc.gamestates;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateManagerTest {
	
	private File directory;
	private GameStateManager gsm;
	
	@BeforeEach
	void setUp() throws IOException {
		
		directory = Files.createTempDirectory("radhoctests").toFile();
		
		gsm = GameStateManagerFactory.createGameStateManager(directory);
		
	}
	
	@Test
	void getEmptyGameStates() {
		
		List<GameState> gameStates = gsm.getAllGameStates();
		assertTrue(gameStates.isEmpty());
		
	}
	
	@Test
	void createGameState() {
		
		gsm.createGameState(GameType.TIC_TAC_TOE, "Dieter", 420, 1337, true);
		
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
		
		gsm.createGameState(GameType.TIC_TAC_TOE, "Lara", 3, 5, true);
		gsm.createGameState(GameType.TIC_TAC_TOE, "Hans", 7, 8, false);
		
		GameState gsLara = gsm.getGameState(5);
		assertEquals("Lara", gsLara.getOpponentName());
		assertEquals(3, gsLara.getOpponentID());
		assertEquals(5, gsLara.getID());
		
		GameState gsHans = gsm.getGameState(8);
		assertEquals("Hans", gsHans.getOpponentName());
		assertEquals(7, gsHans.getOpponentID());
		assertEquals(8, gsHans.getID());
		
		assertThrows(IllegalArgumentException.class, () -> gsm.getGameState(126));
		
	}
	
	@Test
	void saveGameStates() {
		
		gsm.createGameState(GameType.TIC_TAC_TOE, "Sara", 1, 2, true);
		gsm.createGameState(GameType.TIC_TAC_TOE, "Peter", 3, 4, false);
		
		gsm.save();
		gsm = GameStateManagerFactory.createGameStateManager(directory);
		
		GameState gsSara = gsm.getGameState(2);
		assertEquals("Sara", gsSara.getOpponentName());
		assertEquals(1, gsSara.getOpponentID());
		assertEquals(2, gsSara.getID());
		
		GameState gsPeter = gsm.getGameState(4);
		assertEquals("Peter", gsPeter.getOpponentName());
		assertEquals(3, gsPeter.getOpponentID());
		assertEquals(4, gsPeter.getID());
		
		gsm.createGameState(GameType.TIC_TAC_TOE, "Clara", 5, 6, false);
		
		gsm.save();
		gsm = GameStateManagerFactory.createGameStateManager(directory);
		
		gsSara = gsm.getGameState(2);
		assertEquals("Sara", gsSara.getOpponentName());
		assertEquals(1, gsSara.getOpponentID());
		assertEquals(2, gsSara.getID());
		
		gsPeter = gsm.getGameState(4);
		assertEquals("Peter", gsPeter.getOpponentName());
		assertEquals(3, gsPeter.getOpponentID());
		assertEquals(4, gsPeter.getID());
		
		GameState gsClara = gsm.getGameState(6);
		assertEquals("Clara", gsClara.getOpponentName());
		assertEquals(5, gsClara.getOpponentID());
		assertEquals(6, gsClara.getID());
		
	}
	
}