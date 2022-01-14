package radhoc.communication;

import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPPeer;
import radhoc.communication.Communication;
import radhoc.communication.InviteListener;
import radhoc.communication.MoveListener;
import radhoc.gamestates.GameType;

public class MockCommunication implements Communication {
	
	private MoveListener moveListener;
	private InviteListener inviteListener;
	
	private boolean didMove = false;
	private boolean didMoveTwice = false;
	private boolean globalInvited = false;
	private boolean globalInvitedTwice = false;
	private boolean invited = false;
	private boolean invitedTwice = false;
	private boolean accepted = false;
	private boolean acceptedTwice = false;
	
	private long moveRecipientID;
	private long moveGameID;
	private byte[] moveMessage;
	private GameType globalInviteGameType;
	private String inviteRecipientName;
	private GameType inviteGameType;
	private long acceptRecipientID;
	private long acceptGameID;
	private GameType acceptGameType;
	private boolean acceptPlayerStarts = false;
	
	@Override
	public void sendMove(long recipientID, long gameID, byte[] message) {
		
		if (didMove)
			didMoveTwice = true;
		
		moveRecipientID = recipientID;
		moveGameID = gameID;
		moveMessage = message;
		
		didMove = true;
	}
	
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
	public void acceptInvite(long recipientID, long gameID, GameType gameType, boolean recipientStart) {
		
		if (accepted)
			acceptedTwice = true;
		
		acceptRecipientID = recipientID;
		acceptGameID = gameID;
		acceptGameType = gameType;
		acceptPlayerStarts = recipientStart;
		
		accepted = true;
		
	}
	
	@Override
	public void setMoveListener(MoveListener listener) {
		
		moveListener = listener;
		
	}
	
	@Override
	public void setInviteListener(InviteListener listener) {
		
		inviteListener = listener;
		
	}
	
	public void mockMove(long gameID, byte[] message) {
		
		moveListener.onMove(gameID, message);
		
	}
	
	public void mockReceiveInvite(String senderName, long senderID, GameType gameType) {
		
		inviteListener.receiveInvite(senderName, senderID, gameType);
		
	}
	
	public void mockInviteAccepted(String senderName, long senderID, long gameID, GameType gameType, boolean playerStarts) {
		
		inviteListener.inviteAccepted(senderName, senderID, gameID, gameType, playerStarts);
		
	}
	
	public long getAcceptedGameID() {
		
		return acceptGameID;
		
	}
	
	public boolean isAcceptRecipientStarting() {
		
		return acceptPlayerStarts;
		
	}
	
	@Override
	public void onStart(ASAPPeer asapPeer) {
		
	}
	
}