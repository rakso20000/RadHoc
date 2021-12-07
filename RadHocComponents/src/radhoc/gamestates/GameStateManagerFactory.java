package radhoc.gamestates;

import radhoc.gamestates.impl.GameStateManagerImpl;

import java.io.File;

public class GameStateManagerFactory {
	
	public static GameStateManager createGameStateManager(File directory) {
		
		return new GameStateManagerImpl(directory);
		
	}
	
}