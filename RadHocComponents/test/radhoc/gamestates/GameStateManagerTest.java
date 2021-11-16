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

		gsm = new GameStateManagerImpl(directory);

	}

	@Test
	void getEmptyGameStates() {

		List<GameState> gameStates = gsm.getAllGameStates();
		assertEquals(true, gameStates.isEmpty());

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

}