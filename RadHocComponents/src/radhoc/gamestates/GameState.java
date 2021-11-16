package radhoc.gamestates;

public interface GameState {

	GameType getGameType();
	String getOpponentName();
	int getOpponentID();
	int getID();

}