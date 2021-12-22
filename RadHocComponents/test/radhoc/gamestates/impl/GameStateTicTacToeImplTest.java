package radhoc.gamestates.impl;

import org.junit.jupiter.api.Test;
import radhoc.gamestates.GameState.GameResult;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.GameStateTicTacToe.Shape;
import radhoc.gamestates.GameType;
import radhoc.gamestates.MockUpdateListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTicTacToeImplTest {
	
	@Test
	void createGameState() {
		
		GameStateTicTacToe gameStateC = new GameStateTicTacToeImpl("Clara", 3, 5, true);
		GameStateTicTacToe gameStateD = new GameStateTicTacToeImpl("Dieter", 4, 10, false);
		
		assertEquals(GameType.TIC_TAC_TOE, gameStateC.getGameType());
		assertEquals("Clara", gameStateC.getOpponentName());
		assertEquals(3, gameStateC.getOpponentID());
		assertEquals(5, gameStateC.getID());
		assertEquals(GameResult.STILL_PLAYING, gameStateC.getGameResult());
		assertTrue(gameStateC.isPlayable());
		assertEquals(Shape.CROSS, gameStateC.getPlayerShape());
		
		assertEquals(GameType.TIC_TAC_TOE, gameStateD.getGameType());
		assertEquals("Dieter", gameStateD.getOpponentName());
		assertEquals(4, gameStateD.getOpponentID());
		assertEquals(10, gameStateD.getID());
		assertEquals(GameResult.STILL_PLAYING, gameStateD.getGameResult());
		assertFalse(gameStateD.isPlayable());
		assertEquals(Shape.CIRCLE, gameStateD.getPlayerShape());
		
	}
	
	@Test
	void setResult() {
		
		GameStateTicTacToe gameStateA = new GameStateTicTacToeImpl("Alice", 1, 2, true);
		GameStateTicTacToe gameStateB = new GameStateTicTacToeImpl("Bernd", 2, 3, true);
		GameStateTicTacToe gameStateC = new GameStateTicTacToeImpl("Clara", 3, 4, false);
		
		assertEquals(GameResult.STILL_PLAYING, gameStateA.getGameResult());
		assertEquals(GameResult.STILL_PLAYING, gameStateB.getGameResult());
		assertEquals(GameResult.STILL_PLAYING, gameStateC.getGameResult());
		
		assertTrue(gameStateA.isPlayable());
		assertTrue(gameStateB.isPlayable());
		assertFalse(gameStateC.isPlayable());
		
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
		
		GameStateTicTacToe gameState = new GameStateTicTacToeImpl("Dieter", 4, 16, true);
		
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
	void finishTurns() {
		
		GameStateTicTacToe gameStateA = new GameStateTicTacToeImpl("Alice", 1, 15, true);
		GameStateTicTacToe gameStateB = new GameStateTicTacToeImpl("Bernd", 2, 25, false);
		
		assertTrue(gameStateA.isPlayable());
		assertFalse(gameStateB.isPlayable());
		
		gameStateA.playerTurnDone();
		gameStateB.opponentTurnDone();
		
		assertFalse(gameStateA.isPlayable());
		assertTrue(gameStateB.isPlayable());
		
		gameStateA.opponentTurnDone();
		gameStateB.playerTurnDone();
		
		assertTrue(gameStateA.isPlayable());
		assertFalse(gameStateB.isPlayable());
		
	}
	
	@Test
	void accessShapeOutOfBounds() {
		
		GameStateTicTacToe gameState = new GameStateTicTacToeImpl("Bernd", 2, 10, true);
		
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
	void modifyFinishedGame() {
		
		GameStateTicTacToe gameStateWon = new GameStateTicTacToeImpl("Alice", 1, 10, true);
		GameStateTicTacToe gameStateLost = new GameStateTicTacToeImpl("Bernd", 2, 20, true);
		GameStateTicTacToe gameStateDraw = new GameStateTicTacToeImpl("Clara", 3, 30, false);
		
		gameStateWon.win();
		gameStateLost.lose();
		gameStateDraw.draw();
		
		assertThrows(IllegalStateException.class, gameStateWon::win);
		assertThrows(IllegalStateException.class, gameStateWon::lose);
		assertThrows(IllegalStateException.class, gameStateWon::draw);
		assertThrows(IllegalStateException.class, () -> gameStateWon.setShapeAt(0, 2, Shape.CROSS));
		
		assertThrows(IllegalStateException.class, gameStateLost::win);
		assertThrows(IllegalStateException.class, gameStateLost::lose);
		assertThrows(IllegalStateException.class, gameStateLost::draw);
		assertThrows(IllegalStateException.class, () -> gameStateWon.setShapeAt(1, 0, Shape.CROSS));
		
		assertThrows(IllegalStateException.class, gameStateDraw::win);
		assertThrows(IllegalStateException.class, gameStateDraw::lose);
		assertThrows(IllegalStateException.class, gameStateDraw::draw);
		assertThrows(IllegalStateException.class, () -> gameStateWon.setShapeAt(1, 1, Shape.CIRCLE));
		
	}
	
	@Test
	void finishWrongTurns() {
		
		GameStateTicTacToe gameStateA = new GameStateTicTacToeImpl("Alice", 1, 1, true);
		GameStateTicTacToe gameStateB = new GameStateTicTacToeImpl("Bernd", 2, 2, false);
		
		assertThrows(IllegalStateException.class, gameStateA::opponentTurnDone);
		assertThrows(IllegalStateException.class, gameStateB::playerTurnDone);
		
		gameStateA.playerTurnDone();
		gameStateB.opponentTurnDone();
		
		assertThrows(IllegalStateException.class, gameStateA::playerTurnDone);
		assertThrows(IllegalStateException.class, gameStateB::opponentTurnDone);
		
	}
	
	@Test
	void readWriteGameState() throws IOException {
		
		GameStateTicTacToeImpl gameState = new GameStateTicTacToeImpl("Alice", 1, 10, true);
		
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
	
	@Test
	void updateListenerCalled() {
		
		GameStateTicTacToe gameStateA = new GameStateTicTacToeImpl("Alice", 1, 100, true);
		GameStateTicTacToe gameStateB = new GameStateTicTacToeImpl("Bernd", 2, 200, false);
		GameStateTicTacToe gameStateC = new GameStateTicTacToeImpl("Clara", 3, 300, false);
		
		MockUpdateListener listenerA = new MockUpdateListener();
		MockUpdateListener listenerB = new MockUpdateListener();
		MockUpdateListener listenerC = new MockUpdateListener();
		
		gameStateA.setUpdateListener(listenerA);
		gameStateB.setUpdateListener(listenerB);
		gameStateC.setUpdateListener(listenerC);
		
		listenerA.assertNotUpdated();
		listenerB.assertNotUpdated();
		listenerC.assertNotUpdated();
		
		gameStateA.setShapeAt(1, 1, Shape.CROSS);
		gameStateB.setShapeAt(0, 2, Shape.CIRCLE);
		gameStateC.setShapeAt(1, 0, Shape.CIRCLE);
		
		listenerA.assertUpdated();
		listenerB.assertUpdated();
		listenerC.assertUpdated();
		
		gameStateA.playerTurnDone();
		gameStateB.opponentTurnDone();
		gameStateC.opponentTurnDone();
		
		listenerA.assertUpdated();
		listenerB.assertUpdated();
		listenerC.assertUpdated();
		
		assertThrows(IllegalStateException.class, gameStateA::playerTurnDone);
		assertThrows(IllegalStateException.class, gameStateB::opponentTurnDone);
		assertThrows(IllegalStateException.class, gameStateC::opponentTurnDone);
		
		assertThrows(IllegalArgumentException.class, () -> gameStateA.setShapeAt(-1, 0, Shape.CIRCLE));
		assertThrows(IllegalArgumentException.class, () -> gameStateB.setShapeAt(1, 3, Shape.CROSS));
		assertThrows(IllegalArgumentException.class, () -> gameStateC.setShapeAt(2, -2, Shape.CIRCLE));
		
		listenerA.assertNotUpdated();
		listenerB.assertNotUpdated();
		listenerC.assertNotUpdated();
		
		gameStateA.win();
		gameStateB.lose();
		gameStateC.draw();
		
		listenerA.assertUpdated();
		listenerB.assertUpdated();
		listenerC.assertUpdated();
		
		assertThrows(IllegalStateException.class, gameStateA::draw);
		assertThrows(IllegalStateException.class, gameStateB::lose);
		assertThrows(IllegalStateException.class, gameStateC::win);
		
		assertThrows(IllegalStateException.class, () -> gameStateA.setShapeAt(2, 0, Shape.CIRCLE));
		assertThrows(IllegalStateException.class, () -> gameStateB.setShapeAt(1, 1, Shape.CROSS));
		assertThrows(IllegalStateException.class, () -> gameStateC.setShapeAt(0, 1, Shape.CROSS));
		
		listenerA.assertNotUpdated();
		listenerB.assertNotUpdated();
		listenerC.assertNotUpdated();
		
	}
	
}