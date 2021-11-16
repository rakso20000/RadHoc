package radhoc.gamestates.impl;

import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
	public void createGameState(GameType gameType, String opponentName, int opponentID, int gameID) {

		GameState gs = new GameStateImpl(gameType, opponentName, opponentID, gameID);

		gameStates.add(gs);

	}

}