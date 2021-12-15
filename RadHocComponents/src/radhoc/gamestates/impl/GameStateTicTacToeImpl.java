package radhoc.gamestates.impl;

import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.GameType;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class GameStateTicTacToeImpl extends GameStateImpl implements GameStateTicTacToe {
	
	private final Shape[] shapes = new Shape[9];
	
	private GameResult result = GameResult.STILL_PLAYING;
	
	public static GameState fromStream(String opponentName, long opponentID, long gameID, InputStream inputStream) {
		
		return new GameStateTicTacToeImpl(opponentName, opponentID, gameID);
		//TODO
		
	}
	
	public GameStateTicTacToeImpl(String opponentName, long opponentID, long gameID) {
		
		super(GameType.TIC_TAC_TOE, opponentName, opponentID, gameID);
		
		Arrays.fill(shapes, Shape.NONE);
		
	}
	
	@Override
	public Shape getOwnShape() {
		return null; //TODO
	}
	
	@Override
	public Shape getShapeAt(int x, int y) throws IllegalArgumentException {
		
		if (x < 0 || x > 2 || y < 0 || y > 2)
			throw new IllegalArgumentException(String.format("Coordinates for Tic Tac Toe field are out of bounds: (%d, %d)", x, y));
		
		return shapes[x * 3 + y];
		
	}
	
	@Override
	public void setShapeAt(int x, int y, Shape shape) throws IllegalArgumentException {
		
		if (x < 0 || x > 2 || y < 0 || y > 2)
			throw new IllegalArgumentException(String.format("Coordinates for Tic Tac Toe field are out of bounds: (%d, %d)", x, y));
		
		shapes[x * 3 + y] = shape;
		
	}
	
	@Override
	protected void writeSpecifics(OutputStream outputStream) {
		
		//TODO
		
	}
	
	@Override
	public GameResult getGameResult() {
		
		return result;
		
	}
	
	@Override
	public boolean isPlayable() {
		
		//TODO check if it's this player's turn
		return result == GameResult.STILL_PLAYING;
		
	}
	
	@Override
	public void win() {
		
		result = GameResult.VICTORY;
		
	}
	
	@Override
	public void lose() {
		
		result = GameResult.DEFEAT;
		
	}
	
	@Override
	public void draw() {
		
		result = GameResult.DRAW;
		
	}
	
}