package radhoc.gamelogic;

import radhoc.communication.Communication;
import radhoc.communication.InviteListener;
import radhoc.communication.MoveListener;
import radhoc.gamestates.GameType;

import static org.junit.jupiter.api.Assertions.*;

public class MockComunication implements Communication {

	private MoveListener moveListener;
	private boolean didMove = false;
	private boolean didMoveTwice = false;
	private long recipientID;
	private long gameID;
	private byte[] message;


	@Override
	public void sendMove(long recipientID, long gameID, byte[] message) {
		if(didMove) {
			didMoveTwice = true;
		}

		this.recipientID = recipientID;
		this.gameID = gameID;
		this.message = message;

		didMove = true;
	}

	public void assertNotMoved() {
		assertFalse(didMove);
	}

	public void assertMoved(long recipientID, long gameID, byte[] message) {
		assertTrue(didMove);
		assertFalse(didMoveTwice);
		assertEquals(recipientID, this.recipientID);
		assertEquals(gameID, this.gameID);
		assertArrayEquals(message, this.message);
		didMove = false;
	}

	@Override
	public void setMoveListener(MoveListener listener) {

		moveListener = listener;

	}

	public void mockMove(long gameID, byte[] message) {

		moveListener.onMove(gameID, message);

	}


	//Not used in GameLogic

	@Override
	public void sendInvite(GameType gameType) {

	}

	@Override
	public void sendInvite(String name, GameType gameType) {

	}

	@Override
	public void acceptInvite(String userName, long userID, long gameID) {

	}

	@Override
	public void setInviteListener(InviteListener listener) {

	}

}