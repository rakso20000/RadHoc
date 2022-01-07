package radhoc.gamelogic.impl;

import radhoc.communication.Communication;
import radhoc.gamelogic.GameLogicTicTacToe;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.GameStateTicTacToe.Shape;

public class GameLogicTicTacToeImpl extends GameLogicImpl implements GameLogicTicTacToe {

	private GameStateTicTacToe state;
	private Shape ownShape;
	private Shape opponentShape;

	public GameLogicTicTacToeImpl(Communication communication, GameStateTicTacToe gameState) {
		super(communication, gameState);
		state = gameState;
		ownShape = gameState.getPlayerShape();
		opponentShape = ownShape == Shape.CROSS ? Shape.CIRCLE : Shape.CROSS;
	}
	
	@Override
	public boolean playShapeAt(int x, int y) {
		communication.sendMove(state.getOpponentID(), state.getID(), new byte[] {(byte) (x*3+y)});
		state.setShapeAt(x, y, ownShape);
		state.playerTurnDone();
		return true;
	}
}