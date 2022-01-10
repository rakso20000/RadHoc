package radhoc.radhocapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import radhoc.gamestates.GameState;

public class GameStateRecyclerAdapter extends RecyclerView.Adapter<GameStateRecyclerAdapter.ViewHolder> {
	
	private final Activity activity;
	
	private final List<GameState> gameStates;
	
	public GameStateRecyclerAdapter(Activity activity, List<GameState> gameStates) {
		
		this.activity = activity;
		this.gameStates = gameStates;
		
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		
		private final Button playButton;
		private final TextView gameTypeText;
		private final TextView opponentNameText;
		private final TextView statusText;
		
		public ViewHolder(@NonNull View view) {
			super(view);
			
			playButton = view.findViewById(R.id.gamestate_play_button);
			gameTypeText = view.findViewById(R.id.gamestate_gametype);
			opponentNameText = view.findViewById(R.id.gamestate_opponentname);
			statusText = view.findViewById(R.id.status_text);
			
		}
		
	}
	
	@NonNull
	@Override
	public GameStateRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gamestate_item, parent, false);
		
		return new ViewHolder(itemView);
		
	}
	
	@Override
	public void onBindViewHolder(@NonNull GameStateRecyclerAdapter.ViewHolder holder, int position) {
		
		GameState gameState = gameStates.get(position);
		
		String statusText = gameState.isPlayable() ? "Your Turn" : switch (gameState.getResult()) {
			case STILL_PLAYING -> "Enemy Turn";
			case VICTORY -> "Victory";
			case DEFEAT -> "DEFEAT";
			case DRAW -> "DRAW";
		};
		
		holder.playButton.setOnClickListener(new GameStateClickListener(activity, gameState));
		holder.playButton.setText(gameState.isPlayable() ? "Play" : "View");
		holder.gameTypeText.setText(gameState.getGameType().toString());
		holder.opponentNameText.setText("vs " + gameState.getOpponentName());
		holder.statusText.setText(statusText);
		
	}
	
	@Override
	public int getItemCount() {
		
		return gameStates.size();
		
	}
	
}