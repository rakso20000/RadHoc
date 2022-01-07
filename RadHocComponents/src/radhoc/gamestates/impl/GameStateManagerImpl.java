package radhoc.gamestates.impl;

import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameType;
import radhoc.gamestates.UpdateListener;

import java.io.*;
import java.util.*;

public class GameStateManagerImpl implements GameStateManager {
	
	private final File directory;
	
	private final Map<Long, GameState> gameStates = new HashMap<>();
	
	private UpdateListener updateListener;
	
	public GameStateManagerImpl(File directory) {
		
		this.directory = directory;
		
		for (File file : Objects.requireNonNull(directory.listFiles())) {
			
			try {
				
				try (FileInputStream fis = new FileInputStream(file)) {
					
					GameState gameState = GameStateImpl.fromStream(fis);
					
					gameStates.put(gameState.getID(), gameState);
					
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
		
		return new ArrayList<>(gameStates.values());
		
	}
	
	@Override
	public GameState createGameState(GameType gameType, String opponentName, long opponentID, long gameID, boolean playerStarts) {
		
		GameState gameState = GameStateImpl.create(gameType, opponentName, opponentID, gameID, playerStarts);
		
		gameStates.put(gameState.getID(), gameState);
		
		update();

		return gameState;
		
	}
	
	@Override
	public void setUpdateListener(UpdateListener listener) {
		
		updateListener = listener;
		
	}
	
	@Override
	public GameState getGameState(long gameID) {
		
		GameState gameState = gameStates.get(gameID);
		
		if (gameState == null)
			throw new IllegalArgumentException("gameID not found: " + gameID);
		
		return gameState;
		
	}
	
	@Override
	public void save() {
		
		for (GameState gameState : gameStates.values()) {
			
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
	
	private void update() {
		
		if (updateListener != null)
			updateListener.onUpdate();
		
	}
	
}