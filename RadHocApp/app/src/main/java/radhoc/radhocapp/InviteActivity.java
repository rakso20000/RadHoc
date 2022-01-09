package radhoc.radhocapp;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import net.sharksystem.asap.android.apps.ASAPActivity;

import radhoc.gamestates.GameType;
import radhoc.invitations.InvitationManager;
import radhoc.radhocapp.databinding.ActivityInviteBinding;

public class InviteActivity extends ASAPActivity {
	
	private InvitationManager invitationManager;
	
	private ActivityInviteBinding binding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RadHocApp app = (RadHocApp) getApplication();
		invitationManager = app.getInvitationManager();
		
		binding = ActivityInviteBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		setSupportActionBar(binding.toolbar);
		
	}
	
	public void onBack(View view) {
		
		finish();
		
	}
	
	public void onSendInvitation(View view) {
		
		String opponentName = binding.inviteOpponentNameText.getText().toString();
		
		if (opponentName.length() == 0)
			invitationManager.sendInvite(GameType.TIC_TAC_TOE);
		else
			invitationManager.sendInvite(opponentName, GameType.TIC_TAC_TOE);
		
		finish();
		
	}
	
}