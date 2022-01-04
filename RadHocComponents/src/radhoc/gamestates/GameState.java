package radhoc.gamestates;

public interface GameState {
	
	/**
	 * Returns the GameType of this game
	 * @return GameType
	 */
	GameType getGameType();
	
	/**
	 * Returns the opponent's username
	 * @return opponent's username
	 */
	String getOpponentName();
	
	/**
	 * Returns the opponent's id
	 * @return opponent's id
	 */
	long getOpponentID();
	
	/**
	 * Returns this game's id
	 * @return game id
	 */
	long getID();
	
	/**
	 * Returns the outcome of this game or STILL_PLAYING if the game is still ongoing
	 * @return GameResult
	 */
	GameResult getGameResult();
	
	/**
	 * Returns whether this device's user can currently play in this game,
	 * for example because it's their turn or not.
	 * @return true if the user can play, false otherwise
	 */
	boolean isPlayable();
	
	/**
	 * Sets the UpdateListener for this GameState.
	 * The UpdateListener is notified whenever this GameState changes.
	 * @param listener
	 */
	void setUpdateListener(UpdateListener listener); 
}