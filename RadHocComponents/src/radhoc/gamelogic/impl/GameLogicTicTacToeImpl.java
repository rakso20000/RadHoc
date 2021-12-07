package radhoc.gamelogic.impl;

import radhoc.gamelogic.GameLogicTicTacToe;
import radhoc.gamestates.GameState;

public class GameLogicTicTacToeImpl extends GameLogicImpl implements GameLogicTicTacToe {

	public GameLogicTicTacToeImpl(GameState state) {
		super(state);
	}
	
	@Override
	public boolean playShapeAt(int x, int y) {
		return false; //TODO
	}
    
}