package radhoc.gamelogic.impl;

import radhoc.communication.Communication;
import radhoc.gamelogic.GameLogic;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameType;

public abstract class GameLogicImpl implements GameLogic {

	protected Communication communication;
	private GameState state;

	public GameLogicImpl(Communication communication, GameState state) {
		this.communication = communication;
		this.state = state;
	}

	@Override
	public GameType getGameType() {
		return this.state.getGameType();
	}
}