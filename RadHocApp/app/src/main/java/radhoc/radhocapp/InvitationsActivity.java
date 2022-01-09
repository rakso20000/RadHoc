package radhoc.radhocapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import net.sharksystem.asap.android.apps.ASAPActivity;

import radhoc.invitations.InvitationManager;
import radhoc.radhocapp.databinding.ActivityInvitationsBinding;

public class InvitationsActivity extends ASAPActivity {
	
	private InvitationManager invitationManager;
	
	private ActivityInvitationsBinding binding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RadHocApp app = (RadHocApp) getApplication();
		invitationManager = app.getInvitationManager();
		
		binding = ActivityInvitationsBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		Toolbar toolbar = binding.toolbar;
		setSupportActionBar(toolbar);
		CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
		toolBarLayout.setTitle(getTitle());
		
	}
	
	public void onBack(View view) {
		
		finish();
		
	}
	
	public void onCreateInvitation(View view) {
		
		Intent intent = new Intent(this, InviteActivity.class);
		startActivity(intent);
		
	}
	
}