package radhoc.radhocapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import radhoc.gamelogic.GameLogicManager;
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
		
		((GameStateRockPaperScissors) gameStateManager.getGameState(16)).addOpponentShape(GameStateRockPaperScissors.Shape.ROCK);
		((GameStateRockPaperScissors) gameStateManager.getGameState(17)).win();
		((GameStateRockPaperScissors) gameStateManager.getGameState(18)).lose();
		
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