package radhoc.gamestates.impl;

import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GameStateManagerImpl implements GameStateManager {

	public GameStateManagerImpl(Path directory) {
	}

	@Override
	public List<GameState> getAllGameStates() {
		return new ArrayList<>();
	}

}