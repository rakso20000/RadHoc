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

}