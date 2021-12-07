package radhoc.gamestates.impl;

import radhoc.gamestates.GameState;
import radhoc.gamestates.GameType;
import radhoc.gamestates.UpdateListener;

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
	public long getOpponentID() {
		return opponentID;
	}

	@Override
	public long getID() {
		return gameID;
	}

	@Override
	public GameResult getGameResult() {
		return null;
	}

	@Override
	public boolean isPlayable() {
		return false;
	}

	@Override
	public void setUpdateListener(UpdateListener listener) {

	}

}
