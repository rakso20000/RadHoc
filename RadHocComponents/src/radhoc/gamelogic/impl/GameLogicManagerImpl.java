package radhoc.gamelogic.impl;

import radhoc.communication.Communication;
import radhoc.communication.MoveListener;
import radhoc.gamelogic.GameLogic;
import radhoc.gamelogic.GameLogicManager;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.GameType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameLogicManagerImpl implements GameLogicManager, MoveListener {
	
	private final GameStateManager gameStateManager;
	private final Communication communication;
	
	private final Map<GameState, GameLogicImpl> gameLogics = new HashMap<>();
	
	public GameLogicManagerImpl(GameStateManager gameStateManager, Communication communication) {
		
		this.gameStateManager = gameStateManager;
		this.communication = communication;
		
		communication.setMoveListener(this);
		
	}
	
	@Override
	public GameLogic getGameLogic(GameState state) {
		
		return getGameLogicImpl(state);
		
	}
	
	private GameLogicImpl getGameLogicImpl(GameState state) {
		
		GameLogicImpl gameLogic = gameLogics.get(state);
		
		if (gameLogic != null)
			return gameLogic;
		
		gameLogic = switch (state.getGameType()) {
			case TIC_TAC_TOE -> new GameLogicTicTacToeImpl(communication, (GameStateTicTacToe) state);
		};
		
		gameLogics.put(state, gameLogic);
		
		return gameLogic;
		
	}
	
	@Override
	public void onMove(long gameID, byte[] message) {
		
		try {
			
			GameState state = gameStateManager.getGameState(gameID);
			GameLogicImpl gameLogic = getGameLogicImpl(state);
			
			gameLogic.receiveMessage(message);
			
		} catch (IllegalArgumentException e) {
			
			System.err.printf("Incoming message for unknown game with id %d%n", gameID);
			
		} catch (IOException e) {
			
			System.err.printf("Failed to parse incoming message for game with id %d: %s%n", gameID, e.getMessage());
			
		}
		
	}
	
}