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
		
		Intent intent = new Intent(activity, TicTacToeActivity.class);
		intent.putExtra("gameID", gameState.getID());
		activity.startActivity(intent);
		
	}
	
}