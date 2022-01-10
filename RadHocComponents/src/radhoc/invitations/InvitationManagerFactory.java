package radhoc.invitations;

import radhoc.communication.Communication;
import radhoc.gamestates.GameStateManager;
import radhoc.invitations.impl.InvitationManagerImpl;

import java.io.File;

public class InvitationManagerFactory {
	
	public static InvitationManager createInvitationManager(GameStateManager gameStateManager, Communication communication, File invitationsFile) {
		
		return new InvitationManagerImpl(gameStateManager, communication, invitationsFile);
		
	}
	
}