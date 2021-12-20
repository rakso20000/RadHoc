package radhoc.gamestates.impl;

import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.GameType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class GameStateTicTacToeImpl extends GameStateImpl implements GameStateTicTacToe {
	
	private final Shape ownShape;
	private final Shape[] shapes = new Shape[9];
	
	private boolean ownTurn;
	private GameResult result = GameResult.STILL_PLAYING;
	
	public static GameStateTicTacToe fromStream(String opponentName, long opponentID, long gameID, InputStream inputStream) throws IOException {
		
		GameStateTicTacToeImpl gameState = new GameStateTicTacToeImpl(opponentName, opponentID, gameID, true); //TODO
		
		for (int i = 0; i < gameState.shapes.length; ++i)
			gameState.shapes[i] = byteToShape(inputStream.read());
		
		return gameState;
		
	}
	
	private static byte shapeToByte(Shape shape) {
		
		return switch (shape) {
			case NONE -> 0;
			case CROSS -> 1;
			case CIRCLE -> 2;
		};
		
	}
	
	private static Shape byteToShape(int b) throws GameStateParseException {
		
		return switch (b) {
			case 0 -> Shape.NONE;
			case 1 -> Shape.CROSS;
			case 2 -> Shape.CIRCLE;
			default -> throw new GameStateParseException("Encountered invalid shape encoding whilst parsing GameStateTicTacToe: " + b);
		};
		
	}
	
	public GameStateTicTacToeImpl(String opponentName, long opponentID, long gameID, boolean ownTurn) {
		
		super(GameType.TIC_TAC_TOE, opponentName, opponentID, gameID);
		
		ownShape = ownTurn ? Shape.CROSS : Shape.CIRCLE;
		this.ownTurn = ownTurn;
		
		Arrays.fill(shapes, Shape.NONE);
		
	}
	
	@Override
	public Shape getOwnShape() {
		
		return ownShape;
		
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
	public GameResult getGameResult() {
		
		return result;
		
	}
	
	@Override
	public boolean isPlayable() {
		
		return result == GameResult.STILL_PLAYING && ownTurn;
		
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
	
	@Override
	protected void writeSpecifics(OutputStream outputStream) throws IOException {
		
		for (Shape shape : shapes)
			outputStream.write(shapeToByte(shape));
		
	}
	
}