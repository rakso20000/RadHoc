package radhoc.invitations;

import java.util.List;
import radhoc.gamestates.GameType;
import radhoc.gamestates.UpdateListener;

public interface InvitationManager {
	
	/**
	 * Returns all current Invitations
	 * @return List<Invitation>
	 */
	List<Invitation> getInvitations();
	
	/**
	 * Sends an Invitation to all players
	 * @param gameType
	 */
	void sendInvite(GameType gameType);
	
	/**
	 * Sends an Invitation to a specific player by their username
	 * @param name
	 * @param gameType
	 */
	void sendInvite(String name, GameType gameType);
	
	/**
	 * Sets an UpdateListener that is notified whenever the list of Invitations changes
	 * @param listener
	 */
	void setUpdateListener(UpdateListener listener);
	
	/**
	 * Persistently saves all current Invitations
	 */
	void save();
	
}