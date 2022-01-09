package radhoc.radhocapp;

import android.app.Application;

import radhoc.communication.Communication;
import radhoc.gamelogic.GameLogicManager;
import radhoc.gamestates.GameStateManager;
import radhoc.invitations.InvitationManager;

public class RadHocApp extends Application {
	
	private GameStateManager gameStateManager;
	private GameLogicManager gameLogicManager;
	private InvitationManager invitationManager;
	
	public GameStateManager getGameStateManager() {
		return gameStateManager;
	}
	
	public void setGameStateManager(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
	}
	
	public GameLogicManager getGameLogicManager() {
		return gameLogicManager;
	}
	
	public void setGameLogicManager(GameLogicManager gameLogicManager) {
		this.gameLogicManager = gameLogicManager;
	}
	
	public InvitationManager getInvitationManager() {
		return invitationManager;
	}
	
	public void setInvitationManager(InvitationManager invitationManager) {
		this.invitationManager = invitationManager;
	}
	
}