package radhoc.invitations.impl;

import radhoc.communication.Communication;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameType;
import radhoc.invitations.Invitation;

public class InvitationImpl implements Invitation {
	
	private final InvitationManagerImpl invitationManager;
	private final GameStateManager gameStateManager;
	private final Communication communication;
	
	private final String opponentName;
	private final long opponentID;
	private final GameType gameType;
	
	public InvitationImpl(InvitationManagerImpl invitationManager, GameStateManager gameStateManager, Communication communication, String opponentName, long opponentID, GameType gameType) {
		
		this.invitationManager = invitationManager;
		this.gameStateManager = gameStateManager;
		this.communication = communication;
		
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
		
		communication.acceptInvite(opponentID, 0, gameType);
		
		gameStateManager.createGameState(gameType, opponentName, opponentID, 0, true);
		
	}
	
	@Override
	public void decline() {
		
		invitationManager.removeInvitation(this);
		
	}
	
}