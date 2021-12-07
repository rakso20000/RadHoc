package radhoc.gamestates.impl;

import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameType;
import radhoc.gamestates.UpdateListener;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameStateManagerImpl implements GameStateManager {
	
	private List<GameState> gameStates;
	
	public GameStateManagerImpl(Path directory) {
		
		gameStates = new ArrayList<>();
		
	}
	
	@Override
	public List<GameState> getAllGameStates() {
		
		return gameStates;
		
	}
	
	@Override
	public GameState createGameState(GameType gameType, String opponentName, int opponentID, int gameID) {
		
		GameState gs = new GameStateImpl(gameType, opponentName, opponentID, gameID);
		
		gameStates.add(gs);
		return gs;
	
	}
	
	@Override
	public void setUpdateListener(UpdateListener listener) {
		//TODO
	}
	
	@Override
	public GameState getGameState(long gameID) {
		
		Optional<GameState> gameState = gameStates.stream().filter(gs -> gs.getID() == gameID).findAny();
		
		if (gameState.isEmpty())
			throw new IllegalArgumentException("gameID not found: " + gameID);
		
		return gameState.get();
		
	}
	
}