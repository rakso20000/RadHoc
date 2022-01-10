package radhoc.gamestates.impl;

import radhoc.gamestates.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateRockPaperScissorsImpl extends GameStateImpl implements GameStateRockPaperScissors {
	
	private final List<Shape> playerShapes = new ArrayList<>();
	private final List<Shape> opponentShapes = new ArrayList<>();
	
	private int playerScore = 0;
	private int opponentScore = 0;
	
	private boolean playable = true;
	private GameResult result = GameResult.STILL_PLAYING;
	
	private static byte shapeToByte(Shape shape) {
		
		return switch (shape) {
			case ROCK -> 0;
			case PAPER -> 1;
			case SCISSORS -> 2;
		};
		
	}
	
	private static Shape byteToShape(int b) throws GameStateParseException {
		
		return switch (b) {
			case 0 -> Shape.ROCK;
			case 1 -> Shape.PAPER;
			case 2 -> Shape.SCISSORS;
			default -> throw new GameStateParseException("Encountered invalid shape encoding whilst parsing GameStateRockPaperScissors: " + b);
		};
		
	}
	public GameStateRockPaperScissorsImpl(String opponentName, long opponentID, long gameID) {
		super(GameType.ROCK_PAPER_SCISSORS, opponentName, opponentID, gameID);
		
	}
	
	public GameStateRockPaperScissorsImpl(String opponentName, long opponentID, long gameID, InputStream inputStream) throws IOException {
		super(GameType.ROCK_PAPER_SCISSORS, opponentName, opponentID, gameID);
		
		try (
			DataInputStream dis = new DataInputStream(inputStream)
		) {
			
			playerScore = dis.readInt();
			opponentScore = dis.readInt();
			
			playable = dis.readBoolean();
			result = GameResult.fromByte(dis.readByte());
			
			int playerShapesSize = dis.readInt();
			
			for (int i = 0; i < playerShapesSize; ++i)
				playerShapes.add(byteToShape(dis.readByte()));
			
			int opponentShapesSize = dis.readInt();
			
			for (int i = 0; i < opponentShapesSize; ++i)
				opponentShapes.add(byteToShape(dis.readByte()));
			
		}
		
	}
	
	@Override
	public GameResult getResult() {
		
		return result;
		
	}
	
	@Override
	public boolean isPlayable() {
		
		return result == GameResult.STILL_PLAYING && playable;
		
	}
	
	@Override
	public void playerScored() throws IllegalStateException {
		
		if (result != GameResult.STILL_PLAYING)
			throw new IllegalStateException(String.format("Tried to modify a GameState that's already %s", result));
		
		++playerScore;
		
		update();
		
	}
	
	@Override
	public void opponentScored() throws IllegalStateException {
		
		if (result != GameResult.STILL_PLAYING)
			throw new IllegalStateException(String.format("Tried to modify a GameState that's already %s", result));
		
		++opponentScore;
		
		update();
		
	}
	
	@Override
	public int getPlayerScore() {
		
		return playerScore;
		
	}
	
	@Override
	public int getOpponentScore() {
		
		return opponentScore;
		
	}
	
	@Override
	public void addPlayerShape(Shape shape) throws IllegalStateException {
		
		if (result != GameResult.STILL_PLAYING)
			throw new IllegalStateException(String.format("Tried to modify a GameState that's already %s", result));
		
		playerShapes.add(shape);
		
		update();
		
	}
	
	@Override
	public void addOpponentShape(Shape shape) throws IllegalStateException {
		
		if (result != GameResult.STILL_PLAYING)
			throw new IllegalStateException(String.format("Tried to modify a GameState that's already %s", result));
		
		opponentShapes.add(shape);
		
		update();
		
	}
	
	@Override
	public List<Shape> getPlayerShapes() {
		
		return new ArrayList<>(playerShapes);
		
	}
	
	@Override
	public List<Shape> getOpponentShapes() {
		
		return new ArrayList<>(opponentShapes);
		
	}
	
	@Override
	public void setPlayable(boolean playable) {
		
		this.playable = playable;
		
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
			
			dos.writeInt(playerScore);
			dos.writeInt(opponentScore);
			
			dos.writeBoolean(playable);
			dos.writeByte(result.toByte());
			
			dos.writeInt(playerShapes.size());
			
			for (Shape shape : playerShapes)
				dos.writeByte(shapeToByte(shape));
			
			dos.writeInt(opponentShapes.size());
			
			for (Shape shape : opponentShapes)
				dos.writeByte(shapeToByte(shape));
			
		}
		
	}
	
}