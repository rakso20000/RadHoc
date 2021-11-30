package radhoc.gamelogic;

import radhoc.gamestates.GameType;

public interface GameLogic {
	/**
	 * Returns the GameType of this GameLogic
	 * @return GameType
	 */
	GameType getGameType();
}