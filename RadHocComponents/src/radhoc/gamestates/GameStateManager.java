package radhoc.gamestates;

import java.util.List;

public interface GameStateManager {
	/**
	 * Returns GameStates for active games
	 * @return List<GameState>
	 */
	List<GameState> getAllGameStates();
	
	/**
	 * Creates a new GameState of the specified GameType
	 * @param gameType
	 * @param opponentName
	 * @param opponentID
	 * @param gameID
	 * @param playerStarts if true, this player has the first turn of the game (only applicable to some GameTypes)
	 * @return GameState
	 */
	GameState createGameState(GameType gameType, String opponentName, long opponentID, long gameID, boolean playerStarts);
	
	/**
	 * Sets the UpdateListener for this GameState.
	 * The UpdateListener is notified whenever a new GameState is added.
	 * @param listener
	 */
	void setUpdateListener(UpdateListener listener);
	
	/**
	 * Returns the GameState with the given id
	 * @param gameID
	 * @return GameState
	 * @throws IllegalArgumentException no GameState with such gameID was found
	 */
	GameState getGameState(long gameID) throws IllegalArgumentException;
	
	/**
	 * Persistently saves all current GameStates
	 */
	void save();
}