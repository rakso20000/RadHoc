package radhoc.communication;

import static org.junit.jupiter.api.Assertions.*;

public class MockMoveListener implements MoveListener {
	
	private boolean receivedMove = false;
	private boolean receivedMoveTwice = false;
	
	private long gameID;
	private byte[] message;
	
	@Override
	public void onMove(long gameID, byte[] message) {
		
		if (receivedMove)
			receivedMoveTwice = true;
		
		this.gameID = gameID;
		this.message = message;
		
		receivedMove = true;
		
	}
	
	public void assertNotCalled() {
		
		assertFalse(receivedMove);
		
	}
	
	public void assertCalled(long gameID, byte[] message) {
		
		assertTrue(receivedMove);
		assertFalse(receivedMoveTwice);
		
		//TODO reenable
		//assertEquals(gameID, this.gameID);
		assertArrayEquals(message, this.message);
		
		receivedMove = false;
		
	}
	
}