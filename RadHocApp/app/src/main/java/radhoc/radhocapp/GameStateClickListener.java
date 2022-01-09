package radhoc.radhocapp;

import android.view.View;

import radhoc.gamestates.GameState;

public class GameStateClickListener implements View.OnClickListener {
	
	private final GameState gameState;
	
	public GameStateClickListener(GameState gameState) {
		
		this.gameState = gameState;
		
	}
	
	@Override
	public void onClick(View v) {
		
		System.out.println(gameState.getOpponentName());
		
	}
	
}