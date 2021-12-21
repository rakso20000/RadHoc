package radhoc.communication.impl;

import net.sharksystem.SharkException;
import net.sharksystem.asap.*;
import radhoc.communication.Communication;
import radhoc.communication.InviteListener;
import radhoc.communication.MoveListener;
import radhoc.gamestates.GameType;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class CommunicationImpl implements Communication, ASAPMessageReceivedListener {
	
	private static final String MOVE_URI = "radhoc://move";
	private static final String GLOBAL_INVITE_URI = "radhoc://global_invite";
	private static final String INVITE_URI = "radhoc://invite";
	private static final String ACCEPT_INVITE_URI = "radhoc://accept_invite";
	
	private final long userID;
	private final String username;
	
	private MoveListener moveListener;
	private InviteListener inviteListener;
	
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
		
		byte[] bytes;
		
		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos)
		) {
			
			dos.writeLong(recipientID);
			dos.writeLong(gameID);
			dos.write(message);
			
			bytes = baos.toByteArray();
			
		} catch (IOException e) {
			
			throw new AssertionError();
			
		}
		
		try {
			
			asapPeer.sendASAPMessage(ASAP_FORMAT, MOVE_URI, bytes);
			
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
		
		byte[] bytes;
		
		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos)
		) {
			
			dos.writeUTF(username);
			dos.writeLong(userID);
			dos.writeByte(gameType.toByte());
			
			bytes = baos.toByteArray();
			
		} catch (IOException e) {
			
			throw new AssertionError();
			
		}
		
		try {
			
			asapPeer.sendASAPMessage(ASAP_FORMAT, GLOBAL_INVITE_URI, bytes);
			
		} catch (ASAPException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	@Override
	public void sendInvite(String recipientName, GameType gameType) {
		
		byte[] bytes;
		
		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos)
		) {
			
			dos.writeUTF(recipientName);
			dos.writeUTF(username);
			dos.writeLong(userID);
			dos.writeByte(gameType.toByte());
			
			bytes = baos.toByteArray();
			
		} catch (IOException e) {
			
			throw new AssertionError();
			
		}
		
		try {
			
			asapPeer.sendASAPMessage(ASAP_FORMAT, INVITE_URI, bytes);
			
		} catch (ASAPException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	@Override
	public void acceptInvite(long recipientID, long gameID, GameType gameType) {
		
		byte[] bytes;
		
		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos)
		) {
			
			dos.writeLong(recipientID);
			dos.writeUTF(username);
			dos.writeLong(userID);
			dos.writeLong(gameID);
			dos.writeByte(gameType.toByte());
			
			bytes = baos.toByteArray();
			
		} catch (IOException e) {
			
			throw new AssertionError();
			
		}
		
		try {
			
			asapPeer.sendASAPMessage(ASAP_FORMAT, ACCEPT_INVITE_URI, bytes);
			
		} catch (ASAPException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	@Override
	public void setInviteListener(InviteListener listener) {
		
		inviteListener = listener;
		
	}
	
	@Override
	public void asapMessagesReceived(ASAPMessages asapMessages, String s, List<ASAPHop> list) throws IOException {
		
		if (!asapMessages.getFormat().equals(ASAP_FORMAT))
			return;
		
		switch (asapMessages.getURI().toString()) {
		case MOVE_URI -> onMoveMessages(asapMessages.getMessages());
		case GLOBAL_INVITE_URI -> onGlobalInvite(asapMessages.getMessages());
		case INVITE_URI -> onInvite(asapMessages.getMessages());
		case ACCEPT_INVITE_URI -> onInviteAccepted(asapMessages.getMessages());
		default -> System.err.printf("Unknown URI: %s%n", asapMessages.getURI());
		}
		
	}
	
	private void onMoveMessages(Iterator<byte[]> iter) {
		
		while (iter.hasNext()) {
			
			byte[] bytes = iter.next();
			
			long gameID;
			byte[] message;
			
			try (
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				DataInputStream dis = new DataInputStream(bais)
			) {
				
				long recipientID = dis.readLong();
				
				if (recipientID != userID)
					continue;
				
				gameID = dis.readLong();
				
				message = new byte[dis.available()];
				
				dis.readFully(message);
				
			} catch (IOException e) {
				
				//malformed message
				continue;
				
			}
			
			moveListener.onMove(gameID, message);
			
		}
		
	}
	
	private void onGlobalInvite(Iterator<byte[]> iter) {
		
		while (iter.hasNext()) {
			
			byte[] bytes = iter.next();
			
			String senderName;
			long senderID;
			GameType gameType;
			
			try (
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				DataInputStream dis = new DataInputStream(bais)
			) {
				
				senderName = dis.readUTF();
				senderID = dis.readLong();
				gameType = GameType.fromByte(dis.readByte());
				
			} catch (IOException e) {
				
				//malformed message
				continue;
				
			}
			
			inviteListener.receiveInvite(senderName, senderID, gameType);
			
		}
		
	}
	
	private void onInvite(Iterator<byte[]> iter) {
		
		while (iter.hasNext()) {
			
			byte[] bytes = iter.next();
			
			String senderName;
			long senderID;
			GameType gameType;
			
			try (
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				DataInputStream dis = new DataInputStream(bais)
			) {
				
				String recipientName = dis.readUTF();
				
				if (!recipientName.equalsIgnoreCase(username))
					continue;
				
				senderName = dis.readUTF();
				senderID = dis.readLong();
				gameType = GameType.fromByte(dis.readByte());
				
			} catch (IOException e) {
				
				//malformed message
				continue;
				
			}
			
			inviteListener.receiveInvite(senderName, senderID, gameType);
			
		}
		
	}
	
	private void onInviteAccepted(Iterator<byte[]> iter) {
		
		while (iter.hasNext()) {
			
			byte[] bytes = iter.next();
			
			String senderName;
			long senderID;
			long gameID;
			GameType gameType;
			
			try (
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				DataInputStream dis = new DataInputStream(bais)
			) {
				
				long recipientID = dis.readLong();
				
				if (recipientID != userID)
					continue;
				
				senderName = dis.readUTF();
				senderID = dis.readLong();
				gameID = dis.readLong();
				gameType = GameType.fromByte(dis.readByte());
				
			} catch (IOException e) {
				
				//malformed message
				continue;
				
			}
			
			inviteListener.inviteAccepted(senderName, senderID, gameID, gameType);
			
		}
		
	}
	
}