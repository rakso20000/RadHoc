package radhoc.gamelogic.impl;

import radhoc.communication.Communication;
import radhoc.gamelogic.GameLogic;
import radhoc.gamelogic.GameLogicRockPaperScissors;
import radhoc.gamelogic.GameLogicTicTacToe;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateRockPaperScissors;
import radhoc.gamestates.GameStateRockPaperScissors.Shape;
import radhoc.gamestates.GameType;

import java.io.IOException;
import java.util.List;

public class GameLogicRockPaperScissorsImpl extends GameLogicImpl implements GameLogicRockPaperScissors {
	
	private final GameStateRockPaperScissors state;
	
	private static byte shapeToByte(Shape shape) {
		
		return switch (shape) {
			case ROCK -> 0;
			case PAPER -> 1;
			case SCISSORS -> 2;
		};
		
	}
	
	private static Shape byteToShape(int b) throws IOException {
		
		return switch (b) {
			case 0 -> Shape.ROCK;
			case 1 -> Shape.PAPER;
			case 2 -> Shape.SCISSORS;
			default -> throw new IOException("Unexpected content for RockPaperScissors message");
		};
		
	}
	
	public GameLogicRockPaperScissorsImpl(Communication communication, GameStateRockPaperScissors state) {
		super(communication, state);
		
		this.state = state;
		
	}
	
	@Override
	public boolean playShape(Shape shape) {
		
		synchronized (this) {
			
			if (!state.isPlayable())
				return false;
			
			state.addPlayerShape(shape);
			
			communication.sendMove(state.getOpponentID(), state.getID(), new byte[]{shapeToByte(shape)});
			
			checkPlayable();
			checkScore();
			
			return true;
			
		}
		
	}
	
	private void opponentPlayedShape(Shape shape) {
		
		synchronized (this) {
			
			state.addOpponentShape(shape);
			
			checkPlayable();
			checkScore();
			
		}
		
	}
	
	private void checkPlayable() {
		
		state.setPlayable(state.getPlayerShapes().size() <= state.getOpponentShapes().size());
		
	}
	
	private void checkScore() {
		
		List<Shape> playerShapes = state.getPlayerShapes();
		List<Shape> opponentShapes = state.getOpponentShapes();
		
		if (playerShapes.size() != opponentShapes.size())
			return;
		
		int roundsPlayed = playerShapes.size();
		
		int difference = (shapeToByte(playerShapes.get(roundsPlayed - 1)) - shapeToByte(opponentShapes.get(roundsPlayed - 1)) + 3) % 3;
		
		if (difference == 1)
			state.playerScored();
		else if (difference == 2)
			state.opponentScored();
		
		//best of three
		
		if (roundsPlayed == 2) {
			
			if (state.getPlayerScore() >= 2)
				state.win();
			else if (state.getOpponentScore() >= 2)
				state.lose();
			
		} else if (roundsPlayed == 3) {
			
			if (state.getPlayerScore() > state.getOpponentScore())
				state.win();
			else if (state.getPlayerScore() < state.getOpponentScore())
				state.lose();
			else
				state.draw();
			
		}
		
	}
	
	@Override
	public void receiveMessage(byte[] message) throws IOException {
		
		if (message.length != 1)
			throw new IOException("Unexpected length for RockPaperScissors message");
		
		opponentPlayedShape(byteToShape(message[0]));
		
	}
	
}