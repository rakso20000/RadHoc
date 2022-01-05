package radhoc.invitations.impl;

import radhoc.gamestates.GameType;
import radhoc.invitations.Invitation;

public class InvitationImpl implements Invitation {
	
	private final InvitationManagerImpl invitationManager;
	
	private final String opponentName;
	private final long opponentID;
	private final GameType gameType;
	
	public InvitationImpl(InvitationManagerImpl invitationManager, String opponentName, long opponentID, GameType gameType) {
		
		this.invitationManager = invitationManager;
		
		this.opponentName = opponentName;
		this.opponentID = opponentID;
		this.gameType = gameType;
		
	}
	
	@Override
	public String getOpponentName() {
		
		return opponentName;
		
	}
	
	@Override
	public long getOpponentID() {
		
		return opponentID;
		
	}
	
	@Override
	public GameType getGameType() {
		
		return gameType;
		
	}
	
	@Override
	public void accept() {
		
		invitationManager.acceptInvitation(this);
		
	}
	
	@Override
	public void decline() {
		
		invitationManager.removeInvitation(this);
		
	}
	
}