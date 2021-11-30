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
	 * @param userName Name of the user who accepted the invitation
	 * @param userID ID of the user who accepted the invitation
	 * @param gameID ID chosen for the newly commencing game
	 */
	void inviteAccepted(String userName, long userID, long gameID);
	
}