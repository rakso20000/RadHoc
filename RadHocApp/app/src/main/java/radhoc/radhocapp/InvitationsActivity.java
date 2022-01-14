package radhoc.radhocapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import radhoc.gamestates.UpdateListener;
import radhoc.invitations.InvitationManager;
import radhoc.radhocapp.databinding.ActivityInvitationsBinding;

public class InvitationsActivity extends RadHocActivity implements UpdateListener {
	
	private InvitationManager invitationManager;
	
	private ActivityInvitationsBinding binding;
	private InvitationsRecyclerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		invitationManager = app.getInvitationManager();
		
		binding = ActivityInvitationsBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		binding.titleBar.titleText.setText("Invitations");
		binding.titleBar.nextButton.setText("Send Invite");
		
		invitationManager.setUpdateListener(this);
		
		adapter = new InvitationsRecyclerAdapter(invitationManager.getInvitations());
		binding.invitationsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		binding.invitationsRecycler.setAdapter(adapter);
		
	}
	
	public void onBack(View view) {
		
		finish();
		
	}
	
	public void onNext(View view) {
		
		Intent intent = new Intent(this, InviteActivity.class);
		startActivity(intent);
		
	}
	
	@Override
	public void onUpdate() {
		
		adapter.updateInvitations(invitationManager.getInvitations());
		
	}
	
}