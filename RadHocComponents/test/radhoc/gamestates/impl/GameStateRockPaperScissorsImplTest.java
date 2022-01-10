package radhoc.gamestates.impl;

import org.junit.jupiter.api.Test;
import radhoc.gamestates.GameResult;
import radhoc.gamestates.GameStateRockPaperScissors;
import radhoc.gamestates.GameStateRockPaperScissors.Shape;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.GameType;
import radhoc.mock.MockUpdateListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameStateRockPaperScissorsImplTest {
	
	@Test
	void createGameState() {
		
		GameStateRockPaperScissors gameState = new GameStateRockPaperScissorsImpl("Alice", 1, 10);
		
		assertEquals(GameType.ROCK_PAPER_SCISSORS, gameState.getGameType());
		assertEquals("Alice", gameState.getOpponentName());
		assertEquals(1, gameState.getOpponentID());
		assertEquals(10, gameState.getID());
		assertEquals(GameResult.STILL_PLAYING, gameState.getResult());
		assertTrue(gameState.isPlayable());
		assertEquals(0, gameState.getPlayerScore());
		assertEquals(0, gameState.getOpponentScore());
		assertTrue(gameState.getPlayerShapes().isEmpty());
		assertTrue(gameState.getOpponentShapes().isEmpty());
		
	}
	
	@Test
	void setResult() {
		
		GameStateRockPaperScissors gameStateA = new GameStateRockPaperScissorsImpl("Alice", 1, 2);
		GameStateRockPaperScissors gameStateB = new GameStateRockPaperScissorsImpl("Bernd", 2, 3);
		GameStateRockPaperScissors gameStateC = new GameStateRockPaperScissorsImpl("Clara", 3, 4);
		
		assertEquals(GameResult.STILL_PLAYING, gameStateA.getResult());
		assertEquals(GameResult.STILL_PLAYING, gameStateB.getResult());
		assertEquals(GameResult.STILL_PLAYING, gameStateC.getResult());
		
		assertTrue(gameStateA.isPlayable());
		assertTrue(gameStateB.isPlayable());
		assertTrue(gameStateC.isPlayable());
		
		gameStateA.win();
		gameStateB.lose();
		gameStateC.draw();
		
		assertEquals(GameResult.VICTORY, gameStateA.getResult());
		assertEquals(GameResult.DEFEAT, gameStateB.getResult());
		assertEquals(GameResult.DRAW, gameStateC.getResult());
		
		assertFalse(gameStateA.isPlayable());
		assertFalse(gameStateB.isPlayable());
		assertFalse(gameStateC.isPlayable());
		
	}
	
	@Test
	void addScore() {
		
		GameStateRockPaperScissors gameState = new GameStateRockPaperScissorsImpl("Petra", 75, 570);
		
		assertEquals(0, gameState.getPlayerScore());
		assertEquals(0, gameState.getOpponentScore());
		
		gameState.playerScored();
		assertEquals(1, gameState.getPlayerScore());
		assertEquals(0, gameState.getOpponentScore());
		
		gameState.opponentScored();
		assertEquals(1, gameState.getPlayerScore());
		assertEquals(1, gameState.getOpponentScore());
		
		gameState.opponentScored();
		assertEquals(1, gameState.getPlayerScore());
		assertEquals(2, gameState.getOpponentScore());
		
		gameState.playerScored();
		assertEquals(2, gameState.getPlayerScore());
		assertEquals(2, gameState.getOpponentScore());
		
	}
	
	@Test
	void addShapes() {
		
		GameStateRockPaperScissors gameState = new GameStateRockPaperScissorsImpl("Peter", 69, 1239);
		
		assertTrue(gameState.getPlayerShapes().isEmpty());
		assertTrue(gameState.getOpponentShapes().isEmpty());
		
		gameState.addOpponentShape(Shape.ROCK);
		assertTrue(gameState.getPlayerShapes().isEmpty());
		assertArrayEquals(new Shape[] {Shape.ROCK}, gameState.getOpponentShapes().toArray());
		
		gameState.addPlayerShape(Shape.SCISSORS);
		assertArrayEquals(new Shape[] {Shape.SCISSORS}, gameState.getPlayerShapes().toArray());
		assertArrayEquals(new Shape[] {Shape.ROCK}, gameState.getOpponentShapes().toArray());
		
		gameState.addPlayerShape(Shape.PAPER);
		assertArrayEquals(new Shape[] {Shape.SCISSORS, Shape.PAPER}, gameState.getPlayerShapes().toArray());
		assertArrayEquals(new Shape[] {Shape.ROCK}, gameState.getOpponentShapes().toArray());
		
		gameState.addOpponentShape(Shape.ROCK);
		assertArrayEquals(new Shape[] {Shape.SCISSORS, Shape.PAPER}, gameState.getPlayerShapes().toArray());
		assertArrayEquals(new Shape[] {Shape.ROCK, Shape.ROCK}, gameState.getOpponentShapes().toArray());
		
		gameState.addPlayerShape(Shape.SCISSORS);
		assertArrayEquals(new Shape[] {Shape.SCISSORS, Shape.PAPER, Shape.SCISSORS}, gameState.getPlayerShapes().toArray());
		assertArrayEquals(new Shape[] {Shape.ROCK, Shape.ROCK}, gameState.getOpponentShapes().toArray());
		
		gameState.addOpponentShape(Shape.PAPER);
		assertArrayEquals(new Shape[] {Shape.SCISSORS, Shape.PAPER, Shape.SCISSORS}, gameState.getPlayerShapes().toArray());
		assertArrayEquals(new Shape[] {Shape.ROCK, Shape.ROCK, Shape.PAPER}, gameState.getOpponentShapes().toArray());
		
	}
	
	@Test
	void setPlayable() {
		
		GameStateRockPaperScissors gameState = new GameStateRockPaperScissorsImpl("Lara", 15, 105);
		
		assertTrue(gameState.isPlayable());
		
		gameState.setPlayable(true);
		assertTrue(gameState.isPlayable());
		
		gameState.setPlayable(false);
		assertFalse(gameState.isPlayable());
		
		gameState.setPlayable(false);
		assertFalse(gameState.isPlayable());
		
		gameState.setPlayable(true);
		assertTrue(gameState.isPlayable());
		
		gameState.setPlayable(true);
		assertTrue(gameState.isPlayable());
		
		gameState.win();
		assertFalse(gameState.isPlayable());
		
		gameState.setPlayable(true);
		assertFalse(gameState.isPlayable());
		
		gameState.setPlayable(false);
		assertFalse(gameState.isPlayable());
		
	}
	
	@Test
	void modifyFinishedGame() {
		
		GameStateRockPaperScissors gameStateWon = new GameStateRockPaperScissorsImpl("Alice", 1, 10);
		GameStateRockPaperScissors gameStateLost = new GameStateRockPaperScissorsImpl("Bernd", 2, 20);
		GameStateRockPaperScissors gameStateDraw = new GameStateRockPaperScissorsImpl("Clara", 3, 30);
		
		gameStateWon.win();
		gameStateLost.lose();
		gameStateDraw.draw();
		
		assertThrows(IllegalStateException.class, gameStateWon::win);
		assertThrows(IllegalStateException.class, gameStateWon::lose);
		assertThrows(IllegalStateException.class, gameStateWon::draw);
		assertThrows(IllegalStateException.class, gameStateWon::playerScored);
		assertThrows(IllegalStateException.class, gameStateWon::opponentScored);
		assertThrows(IllegalStateException.class, () -> gameStateWon.addPlayerShape(Shape.ROCK));
		assertThrows(IllegalStateException.class, () -> gameStateWon.addOpponentShape(Shape.PAPER));
		
		assertThrows(IllegalStateException.class, gameStateLost::win);
		assertThrows(IllegalStateException.class, gameStateLost::lose);
		assertThrows(IllegalStateException.class, gameStateLost::draw);
		assertThrows(IllegalStateException.class, gameStateLost::playerScored);
		assertThrows(IllegalStateException.class, gameStateLost::opponentScored);
		assertThrows(IllegalStateException.class, () -> gameStateLost.addPlayerShape(Shape.SCISSORS));
		assertThrows(IllegalStateException.class, () -> gameStateLost.addOpponentShape(Shape.ROCK));
		
		assertThrows(IllegalStateException.class, gameStateDraw::win);
		assertThrows(IllegalStateException.class, gameStateDraw::lose);
		assertThrows(IllegalStateException.class, gameStateDraw::draw);
		assertThrows(IllegalStateException.class, gameStateDraw::playerScored);
		assertThrows(IllegalStateException.class, gameStateDraw::opponentScored);
		assertThrows(IllegalStateException.class, () -> gameStateDraw.addPlayerShape(Shape.PAPER));
		assertThrows(IllegalStateException.class, () -> gameStateDraw.addOpponentShape(Shape.SCISSORS));
		
	}
	
	@Test
	void readWriteShapes() throws IOException {
		
		GameStateRockPaperScissorsImpl gameState = new GameStateRockPaperScissorsImpl("Klaus", 40, 4040);
		
		gameState.addPlayerShape(Shape.ROCK);
		gameState.addPlayerShape(Shape.SCISSORS);
		gameState.addPlayerShape(Shape.ROCK);
		gameState.addOpponentShape(Shape.PAPER);
		gameState.addOpponentShape(Shape.PAPER);
		gameState.addOpponentShape(Shape.ROCK);
		
		GameStateRockPaperScissors gameStateNew = readWrite(gameState);
		
		assertArrayEquals(new Shape[] {Shape.ROCK, Shape.SCISSORS, Shape.ROCK}, gameStateNew.getPlayerShapes().toArray());
		assertArrayEquals(new Shape[] {Shape.PAPER, Shape.PAPER, Shape.ROCK}, gameStateNew.getOpponentShapes().toArray());
		
	}
	
	@Test
	void readWriteData() throws IOException {
		
		GameStateRockPaperScissorsImpl gameStateA = new GameStateRockPaperScissorsImpl("Alice", 1, -1);
		GameStateRockPaperScissorsImpl gameStateB = new GameStateRockPaperScissorsImpl("Bernd", 2, -2);
		GameStateRockPaperScissorsImpl gameStateC = new GameStateRockPaperScissorsImpl("Clara", 3, -3);
		GameStateRockPaperScissorsImpl gameStateD = new GameStateRockPaperScissorsImpl("Dieter", 4, -4);
		GameStateRockPaperScissorsImpl gameStateE = new GameStateRockPaperScissorsImpl("Erik", 5, -5);
		
		gameStateB.playerScored();
		gameStateB.opponentScored();
		gameStateB.opponentScored();
		
		gameStateC.playerScored();
		gameStateC.playerScored();
		gameStateC.playerScored();
		
		gameStateD.opponentScored();
		
		gameStateE.playerScored();
		gameStateE.opponentScored();
		
		gameStateB.setPlayable(false);
		gameStateC.setPlayable(false);
		
		gameStateC.win();
		gameStateD.lose();
		gameStateE.draw();
		
		GameStateRockPaperScissors gameStateNewA = readWrite(gameStateA);
		GameStateRockPaperScissors gameStateNewB = readWrite(gameStateB);
		GameStateRockPaperScissors gameStateNewC = readWrite(gameStateC);
		GameStateRockPaperScissors gameStateNewD = readWrite(gameStateD);
		GameStateRockPaperScissors gameStateNewE = readWrite(gameStateE);
		
		assertEquals(0, gameStateNewA.getPlayerScore());
		assertEquals(0, gameStateNewA.getOpponentScore());
		assertEquals(1, gameStateNewB.getPlayerScore());
		assertEquals(2, gameStateNewB.getOpponentScore());
		assertEquals(3, gameStateNewC.getPlayerScore());
		assertEquals(0, gameStateNewC.getOpponentScore());
		assertEquals(0, gameStateNewD.getPlayerScore());
		assertEquals(1, gameStateNewD.getOpponentScore());
		assertEquals(1, gameStateNewE.getPlayerScore());
		assertEquals(1, gameStateNewE.getOpponentScore());
		
		assertTrue(gameStateA.isPlayable());
		assertFalse(gameStateB.isPlayable());
		assertFalse(gameStateC.isPlayable());
		assertFalse(gameStateD.isPlayable());
		assertFalse(gameStateE.isPlayable());
		
		assertEquals(GameResult.STILL_PLAYING, gameStateA.getResult());
		assertEquals(GameResult.STILL_PLAYING, gameStateB.getResult());
		assertEquals(GameResult.VICTORY, gameStateC.getResult());
		assertEquals(GameResult.DEFEAT, gameStateD.getResult());
		assertEquals(GameResult.DRAW, gameStateE.getResult());
		
	}
	
	private GameStateRockPaperScissors readWrite(GameStateRockPaperScissorsImpl gameState) throws IOException {
		
		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream()
		) {
			
			gameState.writeSpecifics(baos);
			
			try (
				ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray())
			) {
				
				return new GameStateRockPaperScissorsImpl(gameState.getOpponentName(), gameState.getOpponentID(), gameState.getID(), bais);
				
			}
			
		}
		
	}
	
	@Test
	void updateListenerCalled() {
		
		MockUpdateListener listenerA = new MockUpdateListener();
		MockUpdateListener listenerB = new MockUpdateListener();
		MockUpdateListener listenerC = new MockUpdateListener();
		
		GameStateRockPaperScissors gameStateA = new GameStateRockPaperScissorsImpl("Alice", 1, 100);
		GameStateRockPaperScissors gameStateB = new GameStateRockPaperScissorsImpl("Bernd", 2, 200);
		GameStateRockPaperScissors gameStateC = new GameStateRockPaperScissorsImpl("Clara", 3, 300);
		
		gameStateA.setUpdateListener(listenerA);
		gameStateB.setUpdateListener(listenerB);
		gameStateC.setUpdateListener(listenerC);
		
		listenerA.assertNotUpdated();
		listenerB.assertNotUpdated();
		listenerC.assertNotUpdated();
		
		gameStateA.playerScored();
		listenerA.assertUpdated();
		gameStateB.opponentScored();
		listenerB.assertUpdated();
		gameStateC.playerScored();
		listenerC.assertUpdated();
		
		gameStateA.addPlayerShape(Shape.ROCK);
		listenerA.assertUpdated();
		gameStateB.addPlayerShape(Shape.PAPER);
		listenerB.assertUpdated();
		gameStateC.addPlayerShape(Shape.SCISSORS);
		listenerC.assertUpdated();
		
		gameStateA.addOpponentShape(Shape.ROCK);
		listenerA.assertUpdated();
		gameStateB.addOpponentShape(Shape.PAPER);
		listenerB.assertUpdated();
		gameStateC.addOpponentShape(Shape.SCISSORS);
		listenerC.assertUpdated();
		
		gameStateA.setPlayable(true);
		listenerA.assertUpdated();
		gameStateB.setPlayable(false);
		listenerB.assertUpdated();
		gameStateC.setPlayable(false);
		listenerC.assertUpdated();
		
		gameStateA.win();
		listenerA.assertUpdated();
		gameStateB.win();
		listenerB.assertUpdated();
		gameStateC.win();
		listenerC.assertUpdated();
		
		assertThrows(IllegalStateException.class, gameStateA::win);
		assertThrows(IllegalStateException.class, gameStateB::draw);
		assertThrows(IllegalStateException.class, gameStateC::lose);
		
		assertThrows(IllegalStateException.class, gameStateA::playerScored);
		assertThrows(IllegalStateException.class, gameStateB::opponentScored);
		assertThrows(IllegalStateException.class, gameStateC::opponentScored);
		
		assertThrows(IllegalStateException.class, () -> gameStateA.addPlayerShape(Shape.ROCK));
		assertThrows(IllegalStateException.class, () -> gameStateB.addPlayerShape(Shape.PAPER));
		assertThrows(IllegalStateException.class, () -> gameStateC.addOpponentShape(Shape.SCISSORS));
		
		listenerA.assertNotUpdated();
		listenerB.assertNotUpdated();
		listenerC.assertNotUpdated();
		
	}
	
}