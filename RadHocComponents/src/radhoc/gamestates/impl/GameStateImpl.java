package radhoc.gamestates.impl;

import radhoc.gamestates.GameState;
import radhoc.gamestates.GameType;
import radhoc.gamestates.UpdateListener;

import java.io.*;

public abstract class GameStateImpl implements GameState {
	
	private final GameType gameType;
	private final String opponentName;
	private final long opponentID;
	private final long gameID;
	
	public static GameState create(GameType gameType, String opponentName, long opponentID, long gameID, boolean playerStarts) {
		
		return switch (gameType) {
			case TIC_TAC_TOE -> new GameStateTicTacToeImpl(opponentName, opponentID, gameID, playerStarts);
		};
		
	}
	
	public static GameState fromStream(InputStream inputStream) throws IOException {
		
		try (ObjectInputStream ois = new ObjectInputStream(inputStream)) {
			
			GameType gameType = GameType.fromByte(ois.readByte());
			String opponentName = ois.readUTF();
			long opponentID = ois.readLong();
			long gameID = ois.readLong();
			
			return switch (gameType) {
				case TIC_TAC_TOE -> GameStateTicTacToeImpl.fromStream(opponentName, opponentID, gameID, inputStream);
			};
			
		}
		
	}
	
	public GameStateImpl(GameType gameType, String opponentName, long opponentID, long gameID) {
		
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
		//TODO
	}
	
	public void writeState(OutputStream outputStream) throws IOException {
		
		try (ObjectOutputStream oos = new ObjectOutputStream(outputStream)) {
			
			oos.writeByte(gameType.toByte());
			oos.writeUTF(opponentName);
			oos.writeLong(opponentID);
			oos.writeLong(gameID);
			
			oos.flush();
			
			writeSpecifics(outputStream);
			
		}
		
	}
	
	protected abstract void writeSpecifics(OutputStream outputStream) throws IOException;
	
}