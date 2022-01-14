package radhoc.radhocapp;

import android.os.Bundle;
import android.view.View;

import java.util.Random;

import radhoc.gamelogic.GameLogicManager;
import radhoc.gamelogic.GameLogicTicTacToe;
import radhoc.gamestates.GameResult;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.GameStateTicTacToe.Shape;
import radhoc.gamestates.UpdateListener;
import radhoc.radhocapp.databinding.ActivityTicTacToeBinding;

public class TicTacToeActivity extends RadHocActivity implements UpdateListener {
	
	private ActivityTicTacToeBinding binding;
	
	private GameStateTicTacToe gameState;
	private GameLogicTicTacToe gameLogic;
	
	private final Random random = new Random();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
		
		if (gameState.getResult() != GameResult.STILL_PLAYING)
			return;
		
		int ox, oy;
		
		do {
			
			ox = random.nextInt(3);
			oy = random.nextInt(3);
			
		} while (gameState.getShapeAt(ox, oy) != Shape.NONE);
		
		app.getCommunication().mockMove(gameState.getID(), new byte[] {(byte) (ox*3 + oy)});
		
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
		
		switch (gameState.getResult()) {
		case VICTORY:
			binding.victoryText.setVisibility(View.VISIBLE);
			break;
		case DEFEAT:
			binding.defeatText.setVisibility(View.VISIBLE);
			break;
		case DRAW:
			binding.drawText.setVisibility(View.VISIBLE);
			break;
		}
		
	}
	
	private CharSequence textForShape(Shape shape) {
		
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