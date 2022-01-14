package radhoc.radhocapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

import radhoc.communication.MockCommunication;
import radhoc.gamelogic.GameLogicManager;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameStateRockPaperScissors;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.GameType;
import radhoc.gamestates.UpdateListener;
import radhoc.radhocapp.databinding.ActivityMainBinding;

public class MainActivity extends RadHocActivity implements UpdateListener {
	
	private GameStateManager gameStateManager;
	
	private ActivityMainBinding binding;
	private GameStateRecyclerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startBluetooth();
		
		gameStateManager = app.getGameStateManager();
		
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		binding.titleBar.titleText.setText("RadHoc");
		binding.titleBar.backButton.setVisibility(View.GONE);
		binding.titleBar.nextButton.setText("Invitations");
		
		//TODO remove sample data
		MockCommunication comm = app.getCommunication();
		
		comm.mockInviteAccepted("Harald", 420, 4, GameType.ROCK_PAPER_SCISSORS, true);
		comm.mockInviteAccepted("Lara", 421, 5, GameType.ROCK_PAPER_SCISSORS, true);
		comm.mockInviteAccepted("Alice", 422, 6, GameType.ROCK_PAPER_SCISSORS, true);
		comm.mockInviteAccepted("Harald", 420, 7, GameType.TIC_TAC_TOE, true);
		comm.mockInviteAccepted("Lara", 421, 8, GameType.TIC_TAC_TOE, true);
		comm.mockInviteAccepted("Alice", 422, 9, GameType.TIC_TAC_TOE, false);
		comm.mockMove(4, new byte[] {2});
		comm.mockMove(9, new byte[] {1*3 + 1});
		
		gameStateManager.setUpdateListener(this);
		
		adapter = new GameStateRecyclerAdapter(this, gameStateManager.getAllGameStates());
		binding.gamestateRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		binding.gamestateRecycler.setAdapter(adapter);
		
	}
	
	public void onNext(View view) {
		
		Intent intent = new Intent(this, InvitationsActivity.class);
		startActivity(intent);
		
	}
	
	@Override
	public void onUpdate() {
		
		adapter.updateGameStates(gameStateManager.getAllGameStates());
		
	}
	
}