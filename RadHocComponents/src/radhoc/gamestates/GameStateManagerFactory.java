package radhoc.gamestates;

import radhoc.gamestates.impl.GameStateManagerImpl;

import java.nio.file.Path;

public class GameStateManagerFactory {
	
	public static GameStateManager createGameStateManager(Path directory) {
		
		return new GameStateManagerImpl(directory);
		
	}
	
}