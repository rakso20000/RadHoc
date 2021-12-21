package radhoc.communication;

import radhoc.gamestates.GameType;

import static org.junit.jupiter.api.Assertions.*;

public class MockInviteListener implements InviteListener {
	
	private boolean invited = false;
	private boolean invitedTwice = false;
	private boolean accepted = false;
	private boolean acceptedTwice = false;
	
	private String receiveSenderName;
	private long recevieSenderID;
	private GameType receiveGameType;
	
	private String acceptUserName;
	private long acceptUserID;
	private long acceptGameID;
	
	@Override
	public void receiveInvite(String senderName, long senderID, GameType gameType) {
		
		if (invited)
			invitedTwice = true;
		
		receiveSenderName = senderName;
		recevieSenderID = senderID;
		receiveGameType = gameType;
		
		invited = true;
		
	}
	
	@Override
	public void inviteAccepted(String userName, long userID, long gameID) {
		
		if (accepted)
			acceptedTwice = true;
		
		acceptUserName = userName;
		acceptUserID = userID;
		acceptGameID = gameID;
		
		accepted = true;
		
	}
	
	public void assertNotInvited() {
		
		assertFalse(invited);
		
	}
	
	public void assertInvited(String senderName, long senderID, GameType gameType) {
		
		assertTrue(invited);
		assertFalse(invitedTwice);
		
		assertEquals(senderName, receiveSenderName);
		assertEquals(senderID, recevieSenderID);
		assertEquals(gameType, receiveGameType);
		
	}
	
	public void assertNotAccepted() {
		
		assertFalse(accepted);
		
	}
	
	public void assertAccepted(String userName, long userID, long gameID) {
		
		assertTrue(accepted);
		assertFalse(acceptedTwice);
		
		assertEquals(userName, acceptUserName);
		assertEquals(userID, acceptUserID);
		assertEquals(gameID, acceptGameID);
		
	}
	
}