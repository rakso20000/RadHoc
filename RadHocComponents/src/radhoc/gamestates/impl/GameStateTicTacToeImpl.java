package radhoc.gamestates.impl;

import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.GameType;

import java.io.InputStream;
import java.io.OutputStream;

public class GameStateTicTacToeImpl extends GameStateImpl implements GameStateTicTacToe {
	
	public static GameState fromStream(String opponentName, long opponentID, long gameID, InputStream inputStream) {
		
		return new GameStateTicTacToeImpl(opponentName, opponentID, gameID);
		//TODO
		
	}
	
	public GameStateTicTacToeImpl(String opponentName, long opponentID, long gameID) {
		
		super(GameType.TIC_TAC_TOE, opponentName, opponentID, gameID);
		
	}
	
	@Override
	public Shape getOwnShape() {
		return null; //TODO
	}
	
	@Override
	public Shape getShapeAt(int x, int y) {
		return null; //TODO
	}
	
	@Override
	public void setShapeAt(int x, int y, Shape shape) {
		//TODO
	}
	
	@Override
	protected void writeSpecifics(OutputStream outputStream) {
		
		//TODO
		
	}
	
	@Override
	public GameResult getGameResult() {
		
		return GameResult.STILL_PLAYING;
		
	}
	
	@Override
	public boolean isPlayable() {
		
		return false;
		
	}
	
}