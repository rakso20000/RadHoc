package radhoc.gamelogic;

import radhoc.communication.Communication;
import radhoc.gamelogic.impl.GameLogicManagerImpl;
import radhoc.gamestates.GameStateManager;

import java.nio.file.Path;

public class GameLogicManagerFactory {
	
	public static GameLogicManager createGameLogicManager(GameStateManager gameStateManager, Communication communication) {
		
		return new GameLogicManagerImpl(gameStateManager, communication);
		
	}
	
}