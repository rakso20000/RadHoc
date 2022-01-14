package radhoc.radhocapp;

import android.app.Application;

import radhoc.communication.Communication;
import radhoc.communication.MockCommunication;
import radhoc.gamelogic.GameLogicManager;
import radhoc.gamestates.GameStateManager;
import radhoc.invitations.InvitationManager;

public class RadHocApp extends Application {
	
	private MockCommunication communication;
	private GameStateManager gameStateManager;
	private GameLogicManager gameLogicManager;
	private InvitationManager invitationManager;
	private boolean initialized;
	
	public MockCommunication getCommunication() {
		return communication;
	}
	
	public void setCommunication(MockCommunication communication) {
		this.communication = communication;
	}
	
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
	
	public boolean isInitialized() {
		return initialized;
	}
	
	public void setInitialized() {
		initialized = true;
	}
	
}