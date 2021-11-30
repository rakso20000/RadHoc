package radhoc.gamelogic;

import radhoc.gamestates.GameState;

public interface GameLogicManager {
	/**
	 * Returns or creates a GameLogic for the given GameState
	 * @param state
	 * @return GameLogic
	 */
	GameLogic getGameLogic(GameState state);
}