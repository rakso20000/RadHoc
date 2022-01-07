package radhoc.gamelogic;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameStateManagerFactory;
import radhoc.gamestates.GameType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameLogicManagerTest {

    private GameStateManager gsm;
    private GameLogicManager glm;

	@BeforeEach
	void setUp() throws IOException {

		Path directory = Files.createTempDirectory("radhoctests");

		gsm = GameStateManagerFactory.createGameStateManager(directory.toFile());
		glm = GameLogicManagerFactory.createGameLogicManager();

	}
	@Test
	void getGameLogicTest() {
		assertEquals(GameType.TIC_TAC_TOE, glm.getGameLogic(gsm.createGameState(GameType.TIC_TAC_TOE, "Dieter", 123, 321, true)).getGameType());
	}
}