package radhoc.gamelogic.impl;

import radhoc.gamelogic.GameLogic;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameType;

public abstract class GameLogicImpl implements GameLogic {

	private GameState state;

	public GameLogicImpl(GameState state) {
		this.state = state;
	}

	@Override
	public GameType getGameType() {
		return this.state.getGameType();
	}
}