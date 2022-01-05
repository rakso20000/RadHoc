package radhoc.invitations;

import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPPeer;
import radhoc.communication.Communication;
import radhoc.communication.InviteListener;
import radhoc.communication.MoveListener;
import radhoc.gamestates.GameType;

import static org.junit.jupiter.api.Assertions.*;

public class MockCommunication implements Communication {
	
	private InviteListener inviteListener;
	
	private boolean globalInvited = false;
	private boolean globalInvitedTwice = false;
	private boolean invited = false;
	private boolean invitedTwice = false;
	private boolean accepted = false;
	private boolean acceptedTwice = false;
	
	private GameType globalInviteGameType;
	private String inviteRecipientName;
	private GameType inviteGameType;
	private long acceptRecipientID;
	private long acceptGameID;
	private GameType acceptGameType;
	
	@Override
	public void sendInvite(GameType gameType) {
		
		if (globalInvited)
			globalInvitedTwice = true;
		
		globalInviteGameType = gameType;
		
		globalInvited = true;
		
	}
	
	@Override
	public void sendInvite(String recipientName, GameType gameType) {
		
		if (invited)
			invitedTwice = true;
		
		inviteRecipientName = recipientName;
		inviteGameType = gameType;
		
		invited = true;
		
	}
	
	@Override
	public void acceptInvite(long recipientID, long gameID, GameType gameType) {
		
		if (accepted)
			acceptedTwice = true;
		
		acceptRecipientID = recipientID;
		acceptGameID = gameID;
		acceptGameType = gameType;
		
		accepted = true;
		
	}
	
	@Override
	public void setInviteListener(InviteListener listener) {
		
		inviteListener = listener;
		
	}
	
	public void mockReceiveInvite(String senderName, long senderID, GameType gameType) {
		
		inviteListener.receiveInvite(senderName, senderID, gameType);
		
	}
	
	public void mockInviteAccepted(String senderName, long senderID, long gameID, GameType gameType) {
		
		inviteListener.inviteAccepted(senderName, senderID, gameID, gameType);
		
	}
	
	public void assertNotGlobalInvited() {
		
		assertFalse(globalInvited);
		
	}
	
	public void assertNotInvited() {
		
		assertFalse(invited);
		
	}
	
	public void assertNotAccepted() {
		
		assertFalse(accepted);
		
	}
	
	public void assertGlobalInvited(GameType gameType) {
		
		assertTrue(globalInvited);
		assertFalse(globalInvitedTwice);
		
		assertEquals(gameType, globalInviteGameType);
		
		globalInvited = false;
		
	}
	
	public void assertInvited(String recipientName, GameType gameType) {
		
		assertTrue(invited);
		assertFalse(invitedTwice);
		
		assertEquals(recipientName, inviteRecipientName);
		assertEquals(gameType, inviteGameType);
		
		invited = false;
		
	}
	
	public void assertAccepted(long recipientID, GameType gameType) {
		
		assertTrue(accepted);
		assertFalse(acceptedTwice);
		
		assertEquals(recipientID, acceptRecipientID);
		assertEquals(gameType, acceptGameType);
		
		accepted = false;
		
	}
	
	public long getAcceptedGameID() {
		
		return acceptGameID;
		
	}
	
	//Not used in Invitations
	
	@Override
	public void onStart(ASAPPeer asapPeer) {
		
	}
	
	@Override
	public void sendMove(long recipientID, long gameID, byte[] message) {
		
	}
	
	@Override
	public void setMoveListener(MoveListener listener) {
		
	}
	
}