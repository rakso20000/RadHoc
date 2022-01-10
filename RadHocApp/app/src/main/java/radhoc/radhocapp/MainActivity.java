package radhoc.radhocapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import net.sharksystem.asap.android.apps.ASAPActivity;

import java.util.Objects;

import radhoc.gamelogic.GameLogicManager;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.GameType;
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
		
		binding.titleBar.titleText.setText("RadHoc");
		binding.titleBar.backButton.setVisibility(View.GONE);
		binding.titleBar.nextButton.setText("Invitations");
		
		//TODO remove sample data
		gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Clara", 5, 10, true);
		gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Lara", 6, 11, true);
		gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Sara", 7, 12, true);
		gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Hans", 8, 13, true);
		gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Peter", 9, 14, true);
		gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Dieter", 10, 15, true);
		gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Georg", 11, 16, true);
		gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Franz", 12, 17, true);
		gameStateManager.createGameState(GameType.ROCK_PAPER_SCISSORS, "Marie", 13, 18, true);
		
		((GameStateTicTacToe) gameStateManager.getGameState(11)).playerTurnDone();
		((GameStateTicTacToe) gameStateManager.getGameState(12)).win();
		((GameStateTicTacToe) gameStateManager.getGameState(13)).lose();
		((GameStateTicTacToe) gameStateManager.getGameState(14)).draw();
		
		RecyclerView recyclerView = findViewById(R.id.gamestate_recycler);
		recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		recyclerView.setAdapter(new GameStateRecyclerAdapter(this, gameStateManager.getAllGameStates()));
		
	}
	
	public void onNext(View view) {
		
		Intent intent = new Intent(this, InvitationsActivity.class);
		startActivity(intent);
		
	}
	
}