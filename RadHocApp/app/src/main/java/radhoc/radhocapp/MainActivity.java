package radhoc.radhocapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.widget.Toolbar;

import android.view.View;

import net.sharksystem.asap.android.apps.ASAPActivity;

import java.util.Objects;

import radhoc.gamelogic.GameLogicManager;
import radhoc.gamestates.GameStateManager;
import radhoc.radhocapp.databinding.ActivityMainBinding;

public class MainActivity extends ASAPActivity {
	
	private GameStateManager gameStateManager;
	private GameLogicManager gameLogicManager;
	
	private ActivityMainBinding binding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RadHocApp app = (RadHocApp) getApplication();
		gameStateManager = Objects.requireNonNull(app.getGameStateManager());
		gameLogicManager = Objects.requireNonNull(app.getGameLogicManager());
		
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		Toolbar toolbar = binding.toolbar;
		setSupportActionBar(toolbar);
		CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
		toolBarLayout.setTitle(getTitle());
		
	}
	
	public void onViewInvitations(View view) {
		
		Intent intent = new Intent(this, InvitationsActivity.class);
		startActivity(intent);
		
	}
	
}