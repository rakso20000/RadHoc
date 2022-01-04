package radhoc.gamestates.impl;

import radhoc.gamestates.GameResult;
import radhoc.gamestates.GameStateParseException;
import radhoc.gamestates.GameStateTicTacToe;
import radhoc.gamestates.GameType;

import java.io.*;
import java.util.Arrays;

public class GameStateTicTacToeImpl extends GameStateImpl implements GameStateTicTacToe {
	
	private final Shape playerShape;
	private final Shape[] shapes = new Shape[9];
	
	private boolean playerTurn;
	private GameResult result = GameResult.STILL_PLAYING;
	
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
	
	public GameStateTicTacToeImpl(String opponentName, long opponentID, long gameID, boolean playerStarts) {
		
		super(GameType.TIC_TAC_TOE, opponentName, opponentID, gameID);
		
		playerShape = playerStarts ? Shape.CROSS : Shape.CIRCLE;
		playerTurn = playerStarts;
		
		Arrays.fill(shapes, Shape.NONE);
		
	}
	
	public GameStateTicTacToeImpl(String opponentName, long opponentID, long gameID, InputStream inputStream) throws IOException {
		
		super(GameType.TIC_TAC_TOE, opponentName, opponentID, gameID);
		
		try (
			DataInputStream dis = new DataInputStream(inputStream)
		) {
			
			playerShape = byteToShape(dis.readByte());
			playerTurn = dis.readBoolean();
			result = GameResult.fromByte(dis.readByte());
			
			for (int i = 0; i < shapes.length; ++i)
				shapes[i] = byteToShape(inputStream.read());
			
		}
		
	}
	
	@Override
	public Shape getPlayerShape() {
		
		return playerShape;
		
	}
	
	@Override
	public Shape getShapeAt(int x, int y) throws IllegalArgumentException {
		
		if (x < 0 || x > 2 || y < 0 || y > 2)
			throw new IllegalArgumentException(String.format("Coordinates for Tic Tac Toe field are out of bounds: (%d, %d)", x, y));
		
		return shapes[x * 3 + y];
		
	}
	
	@Override
	public void setShapeAt(int x, int y, Shape shape) throws IllegalArgumentException, IllegalStateException {
		
		if (x < 0 || x > 2 || y < 0 || y > 2)
			throw new IllegalArgumentException(String.format("Coordinates for Tic Tac Toe field are out of bounds: (%d, %d)", x, y));
		
		if (result != GameResult.STILL_PLAYING)
			throw new IllegalStateException(String.format("Tried to modify the field of a GameState that's already %s", result));
		
		shapes[x * 3 + y] = shape;
		
		update();
		
	}
	
	@Override
	public GameResult getGameResult() {
		
		return result;
		
	}
	
	@Override
	public boolean isPlayable() {
		
		return result == GameResult.STILL_PLAYING && playerTurn;
		
	}
	
	@Override
	public void playerTurnDone() throws IllegalStateException {
		
		if (!playerTurn)
			throw new IllegalStateException("Tried finishing player's turn when it's the opponent's turn");
		
		playerTurn = false;
		
		update();
		
	}
	
	@Override
	public void opponentTurnDone() throws IllegalStateException {
		
		if (playerTurn)
			throw new IllegalStateException("Tried finishing opponent's turn when it's the player's turn");
		
		playerTurn = true;
		
		update();
		
	}
	
	@Override
	public void win() throws IllegalStateException {
		
		if (result != GameResult.STILL_PLAYING)
			throw new IllegalStateException(String.format("Tried to win a GameState that's already %s", result));
		
		result = GameResult.VICTORY;
		
		update();
		
	}
	
	@Override
	public void lose() throws IllegalStateException {
		
		if (result != GameResult.STILL_PLAYING)
			throw new IllegalStateException(String.format("Tried to lose a GameState that's already %s", result));
		
		result = GameResult.DEFEAT;
		
		update();
		
	}
	
	@Override
	public void draw() throws IllegalStateException {
		
		if (result != GameResult.STILL_PLAYING)
			throw new IllegalStateException(String.format("Tried to draw a GameState that's already %s", result));
		
		result = GameResult.DRAW;
		
		update();
		
	}
	
	@Override
	protected void writeSpecifics(OutputStream outputStream) throws IOException {
		
		try (
			DataOutputStream dos = new DataOutputStream(outputStream)
		) {
			
			dos.writeByte(shapeToByte(playerShape));
			dos.writeBoolean(playerTurn);
			dos.writeByte(result.toByte());
			
			for (Shape shape : shapes)
				dos.writeByte(shapeToByte(shape));
			
		}
		
		
	}
	
}