package radhoc.invitations;

import radhoc.communication.Communication;
import radhoc.gamestates.GameStateManager;
import radhoc.invitations.impl.InvitationManagerImpl;

public class InvitationManagerFactory {
	
	public static InvitationManager createInvitationManager(GameStateManager gameStateManager, Communication communication) {
		
		return new InvitationManagerImpl(gameStateManager, communication);
		
	}
	
}