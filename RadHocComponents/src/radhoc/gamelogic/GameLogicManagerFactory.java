package radhoc.gamelogic;

import radhoc.gamelogic.impl.GameLogicManagerImpl;

import java.nio.file.Path;

public class GameLogicManagerFactory {

	public static GameLogicManager createGameLogicManager(Path directory) {

		return new GameLogicManagerImpl(directory);

	}

}
