package radhoc.gamestates.impl;

import radhoc.gamestates.GameState;
import radhoc.gamestates.GameType;

public class GameStateImpl implements GameState {

	private final GameType gameType;
	private final String opponentName;
	private final int opponentID;
	private final int gameID;

	public GameStateImpl(GameType gameType, String opponentName, int opponentID, int gameID) {
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
	public int getOpponentID() {
		return opponentID;
	}

	@Override
	public int getID() {
		return gameID;
	}

}
