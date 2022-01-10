package radhoc.gamestates.impl;

import radhoc.gamestates.GameResult;
import radhoc.gamestates.GameStateRockPaperScissors;
import radhoc.gamestates.GameType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GameStateRockPaperScissorsImpl extends GameStateImpl implements GameStateRockPaperScissors {
	
	public GameStateRockPaperScissorsImpl(String opponentName, long opponentID, long gameID) {
		super(GameType.ROCK_PAPER_SCISSORS, opponentName, opponentID, gameID);
		
	}
	
	public GameStateRockPaperScissorsImpl(String opponentName, long opponentID, long gameID, InputStream inputStream) {
		super(GameType.ROCK_PAPER_SCISSORS, opponentName, opponentID, gameID);
		
	}
	
	@Override
	public GameResult getResult() {
		
		return GameResult.STILL_PLAYING;
		
	}
	
	@Override
	public boolean isPlayable() {
		return false;
	}
	
	@Override
	protected void writeSpecifics(OutputStream outputStream) throws IOException {
		
	}
	
}