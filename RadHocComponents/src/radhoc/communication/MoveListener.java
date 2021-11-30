package radhoc.communication;

import radhoc.gamestates.GameType;

public interface MoveListener {
	/**
	 * Receive all moves other users have sent to this device's user
	 * @param gameID id of the game the move takes place in
	 * @param message encoded move specific to the GameType
	 */
	void onMove(long gameID, byte[] message);
}