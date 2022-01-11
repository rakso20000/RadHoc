package radhoc.radhocapp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import radhoc.gamestates.GameState;

public class GameStateClickListener implements View.OnClickListener {
	
	private final Activity activity;
	private final GameState gameState;
	
	public GameStateClickListener(Activity activity, GameState gameState) {
		
		this.activity = activity;
		this.gameState = gameState;
		
	}
	
	@Override
	public void onClick(View v) {
		
		Class<?> destination = switch (gameState.getGameType()) {
			case TIC_TAC_TOE -> TicTacToeActivity.class;
			case ROCK_PAPER_SCISSORS -> RockPaperScissorsActivity.class;
		};
		
		Intent intent = new Intent(activity, destination);
		intent.putExtra("gameID", gameState.getID());
		activity.startActivity(intent);
		
	}
	
}