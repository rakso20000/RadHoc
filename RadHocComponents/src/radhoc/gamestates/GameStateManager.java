package radhoc.gamestates;

import java.util.List;

public interface GameStateManager {
	/**
	 * returns GameStates for active games
	 * @return List<GameState>
	 */
	List<GameState> getAllGameStates();
	
	/**
	 * creates a new GameState of the specified GameType
	 * @param gameType
	 * @param opponentName
	 * @param opponentID
	 * @param gameID
	 */
	void createGameState(GameType gameType, String opponentName, int opponentID, int gameID);
	
	/**
	 * Sets the UpdateListener for this GameState.
	 * The UpdateListener is notified whenever a new GameState is added.
	 * @param listener
	 */
	void setUpdateListener(UpdateListener listener);
}