package radhoc.radhocapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import net.sharksystem.asap.android.apps.ASAPActivity;

import radhoc.gamelogic.GameLogic;
import radhoc.gamelogic.GameLogicManager;
import radhoc.gamelogic.GameLogicTicTacToe;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.UpdateListener;
import radhoc.radhocapp.databinding.ActivityTicTacToeBinding;

public class TicTacToeActivity extends ASAPActivity implements UpdateListener {
	
	private ActivityTicTacToeBinding binding;
	
	private GameStateTicTacToe gameState;
	private GameLogicTicTacToe gameLogic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RadHocApp app = (RadHocApp) getApplication();
		GameStateManager gameStateManager = app.getGameStateManager();
		GameLogicManager gameLogicManager = app.getGameLogicManager();
		
		long gameID = getIntent().getLongExtra("gameID", 0);
		
		gameState = (GameStateTicTacToe) gameStateManager.getGameState(gameID);
		gameLogic = (GameLogicTicTacToe) gameLogicManager.getGameLogic(gameState);
		
		binding = ActivityTicTacToeBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		binding.titleBar.titleText.setText("TicTacToe - " + gameState.getOpponentName());
		binding.titleBar.nextButton.setVisibility(View.GONE);
		
		gameState.setUpdateListener(this);
		
		updateFields();
		
		binding.field00.setOnClickListener(view -> fieldClicked(0, 0));
		binding.field01.setOnClickListener(view -> fieldClicked(0, 1));
		binding.field02.setOnClickListener(view -> fieldClicked(0, 2));
		binding.field10.setOnClickListener(view -> fieldClicked(1, 0));
		binding.field11.setOnClickListener(view -> fieldClicked(1, 1));
		binding.field12.setOnClickListener(view -> fieldClicked(1, 2));
		binding.field20.setOnClickListener(view -> fieldClicked(2, 0));
		binding.field21.setOnClickListener(view -> fieldClicked(2, 1));
		binding.field22.setOnClickListener(view -> fieldClicked(2, 2));
		
	}
	
	public void onBack(View view) {
		
		finish();
		
	}
	
	private void fieldClicked(int x, int y) {
		
		boolean couldPlay = gameLogic.playShapeAt(x, y);
		
		System.out.printf("X: %d, Y: %d%n", x, y);
		
	}
	
	private void updateFields() {
		
		binding.field00.setText(textForShape(gameState.getShapeAt(0, 0)));
		binding.field01.setText(textForShape(gameState.getShapeAt(0, 1)));
		binding.field02.setText(textForShape(gameState.getShapeAt(0, 2)));
		binding.field10.setText(textForShape(gameState.getShapeAt(1, 0)));
		binding.field11.setText(textForShape(gameState.getShapeAt(1, 1)));
		binding.field12.setText(textForShape(gameState.getShapeAt(1, 2)));
		binding.field20.setText(textForShape(gameState.getShapeAt(2, 0)));
		binding.field21.setText(textForShape(gameState.getShapeAt(2, 1)));
		binding.field22.setText(textForShape(gameState.getShapeAt(2, 2)));
		
	}
	
	private CharSequence textForShape(GameStateTicTacToe.Shape shape) {
		
		return switch (shape) {
			case CROSS -> "X";
			case CIRCLE -> "O";
			case NONE -> "";
		};
		
	}
	
	@Override
	public void onUpdate() {
		
		updateFields();
		
	}
	
}