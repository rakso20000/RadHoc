package radhoc.radhocapp;

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
	
	private final List<GameState> gameStates;
	
	public GameStateRecyclerAdapter(List<GameState> gameStates) {
		
		this.gameStates = gameStates;
		
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		
		private final Button playButton;
		private final TextView gameTypeText;
		private final TextView opponentNameText;
		
		public ViewHolder(@NonNull View view) {
			super(view);
			
			playButton = view.findViewById(R.id.gamestate_play_button);
			gameTypeText = view.findViewById(R.id.gamestate_gametype);
			opponentNameText = view.findViewById(R.id.gamestate_opponentname);
			
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
		
		holder.playButton.setOnClickListener(new GameStateClickListener(gameState));
		holder.gameTypeText.setText(gameState.getGameType().toString());
		holder.opponentNameText.setText(gameState.getOpponentName());
		
	}
	
	@Override
	public int getItemCount() {
		
		return gameStates.size();
		
	}
	
}