package radhoc.gamelogic.impl;

import radhoc.communication.Communication;
import radhoc.gamelogic.GameLogic;
import radhoc.gamelogic.GameLogicManager;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.GameType;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GameLogicManagerImpl implements GameLogicManager {

	private Map<GameState, GameLogic> gameLogics;

    public GameLogicManagerImpl() {
         gameLogics = new HashMap<>();
    }

    @Override
	public GameLogic getGameLogic(GameState state) {
		
		GameLogic gl = gameLogics.get(state);
		
		if(gl != null)
			return gl;
		
		GameType gt = state.getGameType();
		switch (gt) {
			//TODO different GameTypes
		case TIC_TAC_TOE:
			gl = new GameLogicTicTacToeImpl(null, (GameStateTicTacToe) state); //TODO pass communication
			gameLogics.put(state, gl);
			return gl;
		default:
			throw new RuntimeException();
		}
		
	}

}