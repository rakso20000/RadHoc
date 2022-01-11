package radhoc.radhocapp;

import android.os.Bundle;
import android.util.Log;

import net.sharksystem.asap.android.apps.ASAPActivity;

import radhoc.gamestates.GameStateManager;
import radhoc.invitations.InvitationManager;

public class RadHocActivity extends ASAPActivity {
	
	protected RadHocApp app;
	
	private GameStateManager gameStateManager;
	private InvitationManager invitationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		app = (RadHocApp) getApplication();
		
		gameStateManager = app.getGameStateManager();
		invitationManager = app.getInvitationManager();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		gameStateManager.save();
		invitationManager.save();
		
	}
	
}