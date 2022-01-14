package radhoc.radhocapp;

import android.os.Bundle;
import android.view.View;

import java.util.List;
import java.util.Random;

import radhoc.gamelogic.GameLogicManager;
import radhoc.gamelogic.GameLogicRockPaperScissors;
import radhoc.gamestates.GameResult;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameStateRockPaperScissors;
import radhoc.gamestates.GameStateRockPaperScissors.Shape;
import radhoc.gamestates.UpdateListener;
import radhoc.radhocapp.databinding.ActivityRockPaperScissorsBinding;

public class RockPaperScissorsActivity extends RadHocActivity implements UpdateListener {
	
	private ActivityRockPaperScissorsBinding binding;
	
	private GameStateRockPaperScissors gameState;
	private GameLogicRockPaperScissors gameLogic;
	
	private final Random random = new Random();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		GameStateManager gameStateManager = app.getGameStateManager();
		GameLogicManager gameLogicManager = app.getGameLogicManager();
		
		long gameID = getIntent().getLongExtra("gameID", 0);
		
		gameState = (GameStateRockPaperScissors) gameStateManager.getGameState(gameID);
		gameLogic = (GameLogicRockPaperScissors) gameLogicManager.getGameLogic(gameState);
		
		binding = ActivityRockPaperScissorsBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		binding.titleBar.titleText.setText("RockPaperScissors - " + gameState.getOpponentName());
		binding.titleBar.nextButton.setVisibility(View.GONE);
		
		gameState.setUpdateListener(this);
		
		updateShapesPlayed();
		
	}
	
	public void onBack(View view) {
		
		finish();
		
	}
	
	public void rockClicked(View view) {
		
		gameLogic.playShape(Shape.ROCK);
		mockOpponentMove();
		
	}
	
	public void paperClicked(View view) {
		
		gameLogic.playShape(Shape.PAPER);
		mockOpponentMove();
		
	}
	
	public void scissorsClicked(View view) {
		
		gameLogic.playShape(Shape.SCISSORS);
		mockOpponentMove();
		
	}
	
	private void mockOpponentMove() {
		
		if (gameState.getResult() != GameResult.STILL_PLAYING)
			return;
		
		if (gameState.getPlayerShapes().size() > gameState.getOpponentShapes().size())
			app.getCommunication().mockMove(gameState.getID(), new byte[] {(byte) random.nextInt(3)});
		
		if (gameState.getResult() != GameResult.STILL_PLAYING)
			return;
		
		if (gameState.getPlayerShapes().size() == gameState.getOpponentShapes().size() && random.nextBoolean())
			app.getCommunication().mockMove(gameState.getID(), new byte[] {(byte) random.nextInt(3)});
		
	}
	
	private void updateShapesPlayed() {
		
		binding.playerScoreText.setText("Your score: " + gameState.getPlayerScore());
		binding.opponentScoreText.setText("Enemy score: " + gameState.getOpponentScore());
		
		List<Shape> playerShapes = gameState.getPlayerShapes();
		List<Shape> opponentShapes = gameState.getOpponentShapes();
		
		int playerTurns = playerShapes.size();
		int opponentTurns = opponentShapes.size();
		int maxTurns = Math.max(playerTurns, opponentTurns);
		
		binding.vs1.setVisibility(maxTurns >= 1 ? View.VISIBLE : View.INVISIBLE);
		binding.vs2.setVisibility(maxTurns >= 2 ? View.VISIBLE : View.INVISIBLE);
		binding.vs3.setVisibility(maxTurns >= 3 ? View.VISIBLE : View.INVISIBLE);
		
		binding.playerShape1.setVisibility(playerTurns >= 1 ? View.VISIBLE : View.INVISIBLE);
		binding.playerShape2.setVisibility(playerTurns >= 2 ? View.VISIBLE : View.INVISIBLE);
		binding.playerShape3.setVisibility(playerTurns >= 3 ? View.VISIBLE : View.INVISIBLE);
		
		binding.opponentShape1.setVisibility(opponentTurns >= 1 ? View.VISIBLE : View.INVISIBLE);
		binding.opponentShape2.setVisibility(opponentTurns >= 2 ? View.VISIBLE : View.INVISIBLE);
		binding.opponentShape3.setVisibility(opponentTurns >= 3 ? View.VISIBLE : View.INVISIBLE);
		
		if (playerTurns >= 1)
			binding.playerShape1.setText(playerShapes.get(0).toString());
		
		if (playerTurns >= 2)
			binding.playerShape2.setText(playerShapes.get(1).toString());
		
		if (playerTurns >= 3)
			binding.playerShape3.setText(playerShapes.get(2).toString());
		
		if (opponentTurns >= 1)
			binding.opponentShape1.setText(playerTurns >= 1 ? opponentShapes.get(0).toString() : "?");
		
		if (opponentTurns >= 2)
			binding.opponentShape2.setText(playerTurns >= 2 ? opponentShapes.get(1).toString() : "?");
		
		if (opponentTurns >= 3)
			binding.opponentShape3.setText(playerTurns >= 3 ? opponentShapes.get(2).toString() : "?");
		
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
	
	@Override
	public void onUpdate() {
		
		updateShapesPlayed();
		
	}
	
}