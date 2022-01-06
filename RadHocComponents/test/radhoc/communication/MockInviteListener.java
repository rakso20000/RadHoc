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
	
	private String acceptSenderName;
	private long acceptSenderID;
	private long acceptGameID;
	private GameType acceptGameType;
	private boolean acceptPlayerStarts;
	
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
	public void inviteAccepted(String senderName, long senderID, long gameID, GameType gameType, boolean playerStarts) {
		
		if (accepted)
			acceptedTwice = true;
		
		acceptSenderName = senderName;
		acceptSenderID = senderID;
		acceptGameID = gameID;
		acceptGameType = gameType;
		acceptPlayerStarts = playerStarts;
		
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
	
	public void assertAccepted(String senderName, long senderID, long gameID, GameType gameType, boolean playerStarts) {
		
		assertTrue(accepted);
		assertFalse(acceptedTwice);
		
		assertEquals(senderName, acceptSenderName);
		assertEquals(senderID, acceptSenderID);
		assertEquals(gameID, acceptGameID);
		assertEquals(gameType, acceptGameType);
		assertEquals(playerStarts, acceptPlayerStarts);
		
	}
	
}