package radhoc.communication;

import radhoc.gamestates.GameType;

public interface InviteListener {
	
	/**
	 * Receive all open invitations
	 * and those directed at this device's user
	 * @param senderName
	 * @param senderID
	 * @param gameType
	 */
	void receiveInvite(String senderName, long senderID, GameType gameType);
	
	/**
	 * Receive confirmation of an invitation that has been accepted
	 * @param senderName Name of the user who accepted the invitation
	 * @param senderID ID of the user who accepted the invitation
	 * @param gameID ID chosen for the newly commencing game
	 * @param gameType GameType of the newly commencing game
	 * @param playerStarts if true, this player has the first turn of the game (only applicable to some GameTypes)
	 */
	void inviteAccepted(String senderName, long senderID, long gameID, GameType gameType, boolean playerStarts);
	
}