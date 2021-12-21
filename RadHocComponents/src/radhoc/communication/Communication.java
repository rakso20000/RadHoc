package radhoc.communication;

import net.sharksystem.ASAPFormats;
import net.sharksystem.SharkComponent;
import radhoc.gamestates.GameType;

@ASAPFormats(formats = Communication.ASAP_FORMAT)
public interface Communication extends SharkComponent {
	
	String ASAP_FORMAT = "application/radhoc";
	
	/**
	 * Sends a move to another player
	 * @param recipientID id of the recipient
	 * @param gameID id of the game the move takes place in
	 * @param message encoded move specific to the GameType
	 */
	void sendMove(long recipientID, long gameID, byte[] message);
	
	/**
	 * Sets a MoveListener to receive all moves other users have sent to this device's user
	 * @param listener
	 */
	void setMoveListener(MoveListener listener);
	
	/**
	 * Sends an open invitation for a specific GameType.
	 * Any other user can accept the invitation.
	 * @param gameType
	 */
	void sendInvite(GameType gameType);
	
	/**
	 * Sends an invitation for a specific GameType directed to a specific user.
	 * @param recipientName username of the recipient of the invitation
	 * @param gameType
	 */
	void sendInvite(String recipientName, GameType gameType);
	
	/**
	 * Accepts an invitation from another user
	 * and tells said user which game id to use for this game.
	 * @param recipientID ID of the recipient
	 * @param gameID ID for the game, chosen by this device
	 * @param gameType GameType of the new game
	 */
	void acceptInvite(long recipientID, long gameID, GameType gameType);
	
	/**
	 * Sets an InviteListener to receive all open invitations
	 * and those directed at this device's user
	 * @param listener
	 */
	void setInviteListener(InviteListener listener);
}