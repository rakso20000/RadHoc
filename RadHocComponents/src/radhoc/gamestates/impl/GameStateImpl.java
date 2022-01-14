package radhoc.gamestates.impl;

import radhoc.gamestates.GameState;
import radhoc.gamestates.GameType;
import radhoc.gamestates.UpdateListener;

import java.io.*;

public abstract class GameStateImpl implements GameState {
	
	private final GameStateManagerImpl gameStateManager;
	
	private final GameType gameType;
	private final String opponentName;
	private final long opponentID;
	private final long gameID;
	
	private UpdateListener updateListener;
	
	public static GameState create(GameStateManagerImpl gameStateManager, GameType gameType, String opponentName, long opponentID, long gameID, boolean playerStarts) {
		
		return switch (gameType) {
			case TIC_TAC_TOE -> new GameStateTicTacToeImpl(gameStateManager, opponentName, opponentID, gameID, playerStarts);
			case ROCK_PAPER_SCISSORS -> new GameStateRockPaperScissorsImpl(gameStateManager, opponentName, opponentID, gameID);
		};
		
	}
	
	public static GameState fromStream(GameStateManagerImpl gameStateManager, InputStream inputStream) throws IOException {
		
		try (DataInputStream dis = new DataInputStream(inputStream)) {
			
			GameType gameType = GameType.fromByte(dis.readByte());
			String opponentName = dis.readUTF();
			long opponentID = dis.readLong();
			long gameID = dis.readLong();
			
			return switch (gameType) {
				case TIC_TAC_TOE -> new GameStateTicTacToeImpl(gameStateManager, opponentName, opponentID, gameID, inputStream);
				case ROCK_PAPER_SCISSORS -> new GameStateRockPaperScissorsImpl(gameStateManager, opponentName, opponentID, gameID, inputStream);
			};
			
		}
		
	}
	
	public GameStateImpl(GameStateManagerImpl gameStateManager, GameType gameType, String opponentName, long opponentID, long gameID) {
		
		this.gameStateManager = gameStateManager;
		this.gameType = gameType;
		this.opponentName = opponentName;
		this.opponentID = opponentID;
		this.gameID = gameID;
		
	}
	
	@Override
	public GameType getGameType() {
		
		return gameType;
		
	}
	
	@Override
	public String getOpponentName() {
		
		return opponentName;
		
	}
	
	@Override
	public long getOpponentID() {
		
		return opponentID;
		
	}
	
	@Override
	public long getID() {
		
		return gameID;
		
	}
	
	@Override
	public void setUpdateListener(UpdateListener listener) {
		
		updateListener = listener;
		
	}
	
	protected void update() {
		
		if (updateListener != null)
			updateListener.onUpdate();
		
		gameStateManager.update();
		
	}
	
	public void writeState(OutputStream outputStream) throws IOException {
		
		try (DataOutputStream dos = new DataOutputStream(outputStream)) {
			
			dos.writeByte(gameType.toByte());
			dos.writeUTF(opponentName);
			dos.writeLong(opponentID);
			dos.writeLong(gameID);
			
			dos.flush();
			
			writeSpecifics(outputStream);
			
		}
		
	}
	
	protected abstract void writeSpecifics(OutputStream outputStream) throws IOException;
	
}