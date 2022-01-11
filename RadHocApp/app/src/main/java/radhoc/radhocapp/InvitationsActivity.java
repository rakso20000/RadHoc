package radhoc.radhocapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import radhoc.invitations.InvitationManager;
import radhoc.radhocapp.databinding.ActivityInvitationsBinding;

public class InvitationsActivity extends RadHocActivity {
	
	private InvitationManager invitationManager;
	
	private ActivityInvitationsBinding binding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		invitationManager = app.getInvitationManager();
		
		binding = ActivityInvitationsBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		binding.titleBar.titleText.setText("Invitations");
		binding.titleBar.nextButton.setText("Send Invite");
		
		RecyclerView recyclerView = findViewById(R.id.invitations_recycler);
		recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		recyclerView.setAdapter(new InvitationsRecyclerAdapter(invitationManager.getInvitations()));
		
	}
	
	public void onBack(View view) {
		
		finish();
		
	}
	
	public void onNext(View view) {
		
		Intent intent = new Intent(this, InviteActivity.class);
		startActivity(intent);
		
	}
	
}