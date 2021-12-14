package radhoc.communication.impl;

import net.sharksystem.SharkException;
import net.sharksystem.asap.*;
import radhoc.communication.Communication;
import radhoc.communication.InviteListener;
import radhoc.communication.MoveListener;
import radhoc.gamestates.GameType;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class CommunicationImpl implements Communication, ASAPMessageReceivedListener {
	
	private static final String MOVE_URI = "radhoc://move";
	
	private final long userID;
	private final String username;
	
	private MoveListener moveListener;
	
	private ASAPPeer asapPeer;
	
	public CommunicationImpl(long userID, String username) {
		
		this.userID = userID;
		this.username = username;
		
	}
	
	@Override
	public void onStart(ASAPPeer asapPeer) {
		
		this.asapPeer = asapPeer;
		
		asapPeer.addASAPMessageReceivedListener(ASAP_FORMAT, this);
		
	}
	
	@Override
	public void sendMove(long recipientID, long gameID, byte[] message) {
		
		try {
			asapPeer.sendASAPMessage(ASAP_FORMAT, MOVE_URI, message);
		} catch (ASAPException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void setMoveListener(MoveListener listener) {
		
		moveListener = listener;
		
	}
	
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
	
	@Override
	public void asapMessagesReceived(ASAPMessages asapMessages, String s, List<ASAPHop> list) throws IOException {
		
		if (!asapMessages.getFormat().equals(ASAP_FORMAT))
			return;
		
		Iterator<byte[]> iter = asapMessages.getMessages();
		
		while (iter.hasNext()) {
			
			byte[] message = iter.next();
			
			moveListener.onMove(0, message);
			
		}
		
	}
	
}