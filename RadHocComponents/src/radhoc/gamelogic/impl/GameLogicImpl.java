package radhoc.gamelogic.impl;

import radhoc.communication.Communication;
import radhoc.gamelogic.GameLogic;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameType;

import java.io.IOException;

public abstract class GameLogicImpl implements GameLogic {
	
	protected final Communication communication;
	private final GameState state;
	
	public GameLogicImpl(Communication communication, GameState state) {
		
		this.communication = communication;
		this.state = state;
		
	}
	
	@Override
	public GameType getGameType() {
		
		return state.getGameType();
		
	}
	
	public abstract void receiveMessage(byte[] message) throws IOException;
	
}