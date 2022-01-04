package radhoc.invitations.impl;

import radhoc.communication.Communication;
import radhoc.communication.InviteListener;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameType;
import radhoc.invitations.Invitation;
import radhoc.invitations.InvitationManager;

import java.util.ArrayList;
import java.util.List;

public class InvitationManagerImpl implements InvitationManager, InviteListener {
	
	private final GameStateManager gameStateManager;
	private final Communication communication;
	
	private final List<Invitation> invitations = new ArrayList<>();
	
	public InvitationManagerImpl(GameStateManager gameStateManager, Communication communication) {
		
		this.gameStateManager = gameStateManager;
		this.communication = communication;
		
		communication.setInviteListener(this);
		
	}
	
	@Override
	public List<Invitation> getInvitations() {
		
		return invitations;
		
	}
	
	@Override
	public void sendInvite(GameType gameType) {
		
		communication.sendInvite(gameType);
		
	}
	
	@Override
	public void sendInvite(String name, GameType gameType) {
		
		communication.sendInvite(name, gameType);
		
	}
	
	@Override
	public void receiveInvite(String senderName, long senderID, GameType gameType) {
		
		invitations.add(new InvitationImpl(this, gameStateManager, communication, senderName, senderID, gameType));
		
	}
	
	@Override
	public void inviteAccepted(String senderName, long senderID, long gameID, GameType gameType) {
		
		gameStateManager.createGameState(gameType, senderName, senderID, gameID, false);
		
	}
	
	public void removeInvitation(Invitation invitation) {
		
		invitations.remove(invitation);
		
	}
	
}