package radhoc.invitations;

import radhoc.gamestates.GameType;

public interface Invitation {
	/**
	 * Returns name of the player's opponent
	 * @return String
	 */
	String getOpponentName();
	
	/**
	 * Returns the ID of the player's opponent
	 * @return long
	 */
	long getOpponentID();
	
	/**
	 * Returns the GameType of this Invitation
	 * @return GameType
	 */
	GameType getGameType();
	
	/**
	 * Accepts this Invitation
	 */
	void accept();
	
	/**
	 * Declines this invitation
	 */
	void decline();
	
}