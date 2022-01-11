package radhoc.radhocapp;

import android.os.Bundle;
import android.view.View;

import radhoc.gamestates.GameType;
import radhoc.invitations.InvitationManager;
import radhoc.radhocapp.databinding.ActivityInviteBinding;

public class InviteActivity extends RadHocActivity {
	
	private InvitationManager invitationManager;
	
	private ActivityInviteBinding binding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		invitationManager = app.getInvitationManager();
		
		binding = ActivityInviteBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		binding.titleBar.titleText.setText("Send Invite");
		binding.titleBar.nextButton.setVisibility(View.GONE);
		
	}
	
	public void onBack(View view) {
		
		finish();
		
	}
	
	public void onSendInvitation(View view) {
		
		int checkedRadio = binding.gameTypeRadio.getCheckedRadioButtonId();
		
		GameType gameType;
		
		if (checkedRadio == R.id.tic_tac_toe_radio)
			gameType = GameType.TIC_TAC_TOE;
		else if (checkedRadio == R.id.rock_paper_scissors_radio)
			gameType = GameType.ROCK_PAPER_SCISSORS;
		else
			return;
		
		String opponentName = binding.inviteOpponentNameText.getText().toString();
		
		if (opponentName.length() == 0)
			invitationManager.sendInvite(gameType);
		else
			invitationManager.sendInvite(opponentName, gameType);
		
		finish();
		
	}
	
}