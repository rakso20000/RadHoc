package radhoc.gamelogic.impl;

import radhoc.communication.Communication;
import radhoc.gamelogic.GameLogicTicTacToe;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.GameStateTicTacToe.Shape;

import java.io.IOException;

public class GameLogicTicTacToeImpl extends GameLogicImpl implements GameLogicTicTacToe {
	
	private final GameStateTicTacToe state;
	private final Shape ownShape;
	private final Shape opponentShape;
	
	public GameLogicTicTacToeImpl(Communication communication, GameStateTicTacToe gameState) {
		super(communication, gameState);
		
		state = gameState;
		ownShape = gameState.getPlayerShape();
		opponentShape = ownShape == Shape.CROSS ? Shape.CIRCLE : Shape.CROSS;
		
	}
	
	@Override
	public boolean playShapeAt(int x, int y) throws IllegalArgumentException {
		
		if (!state.isPlayable())
			return false;
		
		if (state.getShapeAt(x, y) != Shape.NONE) //Pass IllegalArgumentException for out of bounds coordinates
			return false;
		
		state.setShapeAt(x, y, ownShape);
		state.playerTurnDone();
		
		if (checkVictory(x, y, ownShape))
			state.win();
		else if (checkAllFilled())
			state.draw();
		
		communication.sendMove(state.getOpponentID(), state.getID(), new byte[] {(byte) (x*3 + y)});
		
		return true;
		
	}
	
	private void opponentPlayedShapeAt(int x, int y) {
		
		state.setShapeAt(x, y, opponentShape);
		state.opponentTurnDone();
		
		if (checkVictory(x, y, opponentShape))
			state.lose();
		else if (checkAllFilled())
			state.draw();
		
	}
	
	private boolean checkVictory(int x, int y, Shape shapePlayed) {
		
		for (int i = 0;; ++i) {
			
			if (i > 2)
				return true;
			
			if (state.getShapeAt(x, i) != shapePlayed)
				break;
			
		}
		
		for (int i = 0;; ++i) {
			
			if (i > 2)
				return true;
			
			if (state.getShapeAt(i, y) != shapePlayed)
				break;
			
		}
		
		return (x == y && state.getShapeAt(0, 0) == shapePlayed && state.getShapeAt(1, 1) == shapePlayed && state.getShapeAt(2, 2) == shapePlayed)
			|| (x + y == 2 && state.getShapeAt(0, 2) == shapePlayed && state.getShapeAt(1, 1) == shapePlayed && state.getShapeAt(2, 0) == shapePlayed);
		
	}
	
	private boolean checkAllFilled() {
		
		for (int x = 0; x < 3; ++x)
			for (int y = 0; y < 3; ++y)
				if (state.getShapeAt(x, y) == Shape.NONE)
					return false;
		
		return true;
		
	}
	
	@Override
	public void receiveMessage(byte[] message) throws IOException {
		
		if (message.length != 1)
			throw new IOException("Unexpected length for TicTacToe message");
		
		if (message[0] < 0 || message[0] > 8)
			throw new IOException("Unexpected content for TicTacToe message");
		
		opponentPlayedShapeAt(message[0] / 3, message[0] % 3);
		
	}
	
}