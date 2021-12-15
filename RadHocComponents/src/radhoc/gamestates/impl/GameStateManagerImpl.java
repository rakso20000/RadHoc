package radhoc.gamestates.impl;

import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameType;
import radhoc.gamestates.UpdateListener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GameStateManagerImpl implements GameStateManager {
	
	private final File directory;
	
	private final List<GameState> gameStates = new ArrayList<>();
	
	public GameStateManagerImpl(File directory) {
		
		this.directory = directory;
		
		for (File file : Objects.requireNonNull(directory.listFiles())) {
			
			try {
				
				try (FileInputStream fis = new FileInputStream(file)) {
					
					gameStates.add(GameStateImpl.fromStream(fis));
					
				}
				
			} catch (IOException ex) {
				
				System.err.println("Failed to load GameState from file " + file.getName());
				ex.printStackTrace();
				
				//noinspection ResultOfMethodCallIgnored
				file.delete();
				
			}
			
		}
		
	}
	
	@Override
	public List<GameState> getAllGameStates() {
		
		return gameStates;
		
	}
	
	@Override
	public void createGameState(GameType gameType, String opponentName, int opponentID, int gameID) {
		
		GameState gs = GameStateImpl.create(gameType, opponentName, opponentID, gameID);
		
		gameStates.add(gs);
		
	}
	
	@Override
	public void setUpdateListener(UpdateListener listener) {
		//TODO
	}
	
	@Override
	public GameState getGameState(long gameID) {
		
		Optional<GameState> gameState = gameStates.stream()
			.filter(gs -> gs.getID() == gameID)
			.findAny();
		
		if (gameState.isEmpty())
			throw new IllegalArgumentException("gameID not found: " + gameID);
		
		return gameState.get();
		
	}
	
	@Override
	public void save() {
		
		for (GameState gameState : gameStates) {
			
			try {
				
				try (FileOutputStream fos = new FileOutputStream(new File(directory, gameState.getID() + ""))) {
					
					((GameStateImpl) gameState).writeState(fos);
					
				}
				
			} catch (IOException ex) {
				
				System.err.println("Failed to save GameState with ID " + gameState.getID());
				ex.printStackTrace();
				
			}
			
		}
		
	}
	
}