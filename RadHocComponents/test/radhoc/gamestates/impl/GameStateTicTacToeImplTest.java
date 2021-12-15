package radhoc.gamestates.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameState.GameResult;
import radhoc.gamestates.GameType;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTicTacToeImplTest {
	
	@Test
	void createGameState() {
		
		GameStateTicTacToeImpl gameState = new GameStateTicTacToeImpl("Clara", 3, 5);
		
		assertEquals(GameType.TIC_TAC_TOE, gameState.getGameType());
		assertEquals("Clara", gameState.getOpponentName());
		assertEquals(3, gameState.getOpponentID());
		assertEquals(5, gameState.getID());
		assertEquals(GameResult.STILL_PLAYING, gameState.getGameResult());
		
	}
	
}