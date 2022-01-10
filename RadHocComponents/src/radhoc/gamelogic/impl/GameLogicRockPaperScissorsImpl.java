package radhoc.gamelogic.impl;

import radhoc.communication.Communication;
import radhoc.gamelogic.GameLogic;
import radhoc.gamelogic.GameLogicRockPaperScissors;
import radhoc.gamelogic.GameLogicTicTacToe;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateRockPaperScissors;
import radhoc.gamestates.GameType;

import java.io.IOException;

public class GameLogicRockPaperScissorsImpl extends GameLogicImpl implements GameLogicRockPaperScissors {
	
	public GameLogicRockPaperScissorsImpl(Communication communication, GameStateRockPaperScissors state) {
		super(communication, state);
		
	}
	
	@Override
	public GameType getGameType() {
		return null;
	}
	
	@Override
	public void receiveMessage(byte[] message) throws IOException {
		
	}
	
}