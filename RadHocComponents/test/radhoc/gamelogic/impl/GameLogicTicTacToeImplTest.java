package radhoc.gamelogic.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import radhoc.communication.Communication;
import radhoc.gamelogic.GameLogicTicTacToe;
import radhoc.gamelogic.MockComunication;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.GameStateTicTacToe.Shape;
import radhoc.gamestates.impl.GameStateTicTacToeImpl;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicTicTacToeImplTest {

	private MockComunication mockComunication;

	@BeforeEach
	void setUp() {

		mockComunication = new MockComunication();

	}

	@Test
	void playShapeAt() {

		GameStateTicTacToe gameState = new GameStateTicTacToeImpl("Clara", 3, 5, true);
		GameLogicTicTacToeImpl gameLogic = new GameLogicTicTacToeImpl(mockComunication, gameState);

		mockComunication.assertNotMoved();
		boolean result = gameLogic.playShapeAt(2, 1);

		assertTrue(result);
		assertFalse(gameState.isPlayable());
		assertEquals(Shape.CROSS, gameState.getShapeAt(2, 1));

		mockComunication.assertMoved(3, 5, new byte[] {2*3+1}); // x: 2; y:1

	}

}