package radhoc.gamestates;

import java.util.List;

public interface GameStateManager {

	List<GameState> getAllGameStates();

	void createGameState(GameType gameType, String opponentName, int opponentID, int gameID);
}