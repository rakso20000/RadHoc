package radhoc.gamestates.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameState.GameResult;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.GameStateTicTacToe.Shape;
import radhoc.gamestates.GameType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTicTacToeImplTest {
	
	@Test
	void createGameState() {
		
		GameStateTicTacToe gameState = new GameStateTicTacToeImpl("Clara", 3, 5);
		
		assertEquals(GameType.TIC_TAC_TOE, gameState.getGameType());
		assertEquals("Clara", gameState.getOpponentName());
		assertEquals(3, gameState.getOpponentID());
		assertEquals(5, gameState.getID());
		assertEquals(GameResult.STILL_PLAYING, gameState.getGameResult());
		
	}
	
	@Test
	void setResult() {
		
		GameStateTicTacToe gameStateA = new GameStateTicTacToeImpl("Alice", 1, 2);
		GameStateTicTacToe gameStateB = new GameStateTicTacToeImpl("Bernd", 2, 3);
		GameStateTicTacToe gameStateC = new GameStateTicTacToeImpl("Clara", 3, 4);
		
		assertEquals(GameResult.STILL_PLAYING, gameStateA.getGameResult());
		assertEquals(GameResult.STILL_PLAYING, gameStateB.getGameResult());
		assertEquals(GameResult.STILL_PLAYING, gameStateC.getGameResult());
		
		gameStateA.win();
		gameStateB.lose();
		gameStateC.draw();
		
		assertEquals(GameResult.VICTORY, gameStateA.getGameResult());
		assertEquals(GameResult.DEFEAT, gameStateB.getGameResult());
		assertEquals(GameResult.DRAW, gameStateC.getGameResult());
		
		assertFalse(gameStateA.isPlayable());
		assertFalse(gameStateB.isPlayable());
		assertFalse(gameStateC.isPlayable());
		
	}
	
	@Test
	void setShapes() {
		
		GameStateTicTacToe gameState = new GameStateTicTacToeImpl("Dieter", 4, 16);
		
		assertEquals(Shape.NONE, gameState.getShapeAt(0, 0));
		assertEquals(Shape.NONE, gameState.getShapeAt(0, 1));
		assertEquals(Shape.NONE, gameState.getShapeAt(0, 2));
		assertEquals(Shape.NONE, gameState.getShapeAt(1, 0));
		assertEquals(Shape.NONE, gameState.getShapeAt(1, 1));
		assertEquals(Shape.NONE, gameState.getShapeAt(1, 2));
		assertEquals(Shape.NONE, gameState.getShapeAt(2, 0));
		assertEquals(Shape.NONE, gameState.getShapeAt(2, 1));
		assertEquals(Shape.NONE, gameState.getShapeAt(2, 2));
		
		gameState.setShapeAt(2, 0, Shape.CROSS);
		gameState.setShapeAt(0, 0, Shape.CIRCLE);
		gameState.setShapeAt(0, 2, Shape.CROSS);
		gameState.setShapeAt(1, 1, Shape.CIRCLE);
		gameState.setShapeAt(2, 2, Shape.CROSS);
		gameState.setShapeAt(2, 1, Shape.CIRCLE);
		gameState.setShapeAt(1, 2, Shape.CROSS);
		
		assertEquals(Shape.CIRCLE, gameState.getShapeAt(0, 0));
		assertEquals(Shape.NONE, gameState.getShapeAt(0, 1));
		assertEquals(Shape.CROSS, gameState.getShapeAt(0, 2));
		assertEquals(Shape.NONE, gameState.getShapeAt(1, 0));
		assertEquals(Shape.CIRCLE, gameState.getShapeAt(1, 1));
		assertEquals(Shape.CROSS, gameState.getShapeAt(1, 2));
		assertEquals(Shape.CROSS, gameState.getShapeAt(2, 0));
		assertEquals(Shape.CIRCLE, gameState.getShapeAt(2, 1));
		assertEquals(Shape.CROSS, gameState.getShapeAt(2, 2));
		
	}
	
	@Test
	void accessShapeOutOfBounds() {
		
		GameStateTicTacToe gameState = new GameStateTicTacToeImpl("Bernd", 2, 10);
		
		assertThrows(IllegalArgumentException.class, () -> gameState.getShapeAt(1, -1));
		assertThrows(IllegalArgumentException.class, () -> gameState.getShapeAt(1, 3));
		assertThrows(IllegalArgumentException.class, () -> gameState.getShapeAt(-1, 1));
		assertThrows(IllegalArgumentException.class, () -> gameState.getShapeAt(3, 1));
		
		assertThrows(IllegalArgumentException.class, () -> gameState.setShapeAt(1, -1, Shape.CROSS));
		assertThrows(IllegalArgumentException.class, () -> gameState.setShapeAt(1, 3, Shape.CROSS));
		assertThrows(IllegalArgumentException.class, () -> gameState.setShapeAt(-1, 1, Shape.CROSS));
		assertThrows(IllegalArgumentException.class, () -> gameState.setShapeAt(3, 1, Shape.CROSS));
		
	}
	
	@Test
	void readWriteGameState() throws IOException {
		
		GameStateTicTacToeImpl gameState = new GameStateTicTacToeImpl("Alice", 1, 10);
		
		gameState.setShapeAt(1, 1, Shape.CROSS);
		gameState.setShapeAt(0, 1, Shape.CIRCLE);
		gameState.setShapeAt(0, 2, Shape.CROSS);
		gameState.setShapeAt(2, 0, Shape.CIRCLE);
		gameState.setShapeAt(2, 2, Shape.CROSS);
		gameState.setShapeAt(1, 2, Shape.CIRCLE);
		gameState.setShapeAt(0, 0, Shape.CROSS);
		
		byte[] bytes;
		
		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream()
		) {
			
			gameState.writeSpecifics(baos);
			
			bytes = baos.toByteArray();
			
		}
		
		GameStateTicTacToe gameStateNew;
		
		try (
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes)
		) {
			
			gameStateNew = GameStateTicTacToeImpl.fromStream("Alice", 1, 10, bais);
			
		}
		
		assertEquals(Shape.CROSS, gameStateNew.getShapeAt(0, 0));
		assertEquals(Shape.CIRCLE, gameStateNew.getShapeAt(0, 1));
		assertEquals(Shape.CROSS, gameStateNew.getShapeAt(0, 2));
		assertEquals(Shape.NONE, gameStateNew.getShapeAt(1, 0));
		assertEquals(Shape.CROSS, gameStateNew.getShapeAt(1, 1));
		assertEquals(Shape.CIRCLE, gameStateNew.getShapeAt(1, 2));
		assertEquals(Shape.CIRCLE, gameStateNew.getShapeAt(2, 0));
		assertEquals(Shape.NONE, gameStateNew.getShapeAt(2, 1));
		assertEquals(Shape.CROSS, gameStateNew.getShapeAt(2, 2));
		
	}
	
}