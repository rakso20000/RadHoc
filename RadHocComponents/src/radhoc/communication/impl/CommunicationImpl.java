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
import java.util.concurrent.CompletableFuture;

public class CommunicationImpl implements Communication, ASAPMessageReceivedListener {
	
	private static final String MOVE_URI = "radhoc://move";
	private static final String GLOBAL_INVITE_URI = "radhoc://global_invite";
	private static final String INVITE_URI = "radhoc://invite";
	private static final String ACCEPT_INVITE_URI = "radhoc://accept_invite";
	
	private final long userID;
	private final String username;
	private final CompletableFuture<Communication> communicationFuture;
	
	private MoveListener moveListener;
	private InviteListener inviteListener;
	
	private ASAPPeer asapPeer;
	
	public CommunicationImpl(long userID, String username, CompletableFuture<Communication> communicationFuture) {
		
		this.userID = userID;
		this.username = username;
		this.communicationFuture = communicationFuture;
		
	}
	
	@Override
	public void onStart(ASAPPeer asapPeer) {
		
		this.asapPeer = asapPeer;
		
		asapPeer.addASAPMessageReceivedListener(ASAP_FORMAT, this);
		
		communicationFuture.complete(this);
		
	}
	
	@Override
	public void setMoveListener(MoveListener listener) {
		
		moveListener = listener;
		
	}
	
	@Override
	public void setInviteListener(InviteListener listener) {
		
		inviteListener = listener;
		
	}
	
	private void sendData(String uri, byte[] data) {
		
		try {
			
			asapPeer.sendASAPMessage(ASAP_FORMAT, uri, data);
			
		} catch (ASAPException e) {
			
			System.err.printf("Failed sending ASAP message with URI %s%n", uri);
			e.printStackTrace();
			
		}
		
	}
	
	@Override
	public void sendMove(long recipientID, long gameID, byte[] message) {
		
		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos)
		) {
			
			dos.writeLong(recipientID);
			dos.writeLong(gameID);
			dos.write(message);
			
			sendData(MOVE_URI, baos.toByteArray());
			
		} catch (IOException e) {
			
			throw new AssertionError();
			
		}
		
	}
	
	@Override
	public void sendInvite(GameType gameType) {
		
		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos)
		) {
			
			dos.writeUTF(username);
			dos.writeLong(userID);
			dos.writeByte(gameType.toByte());
			
			sendData(GLOBAL_INVITE_URI, baos.toByteArray());
			
		} catch (IOException e) {
			
			throw new AssertionError();
			
		}
		
	}
	
	@Override
	public void sendInvite(String recipientName, GameType gameType) {
		
		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos)
		) {
			
			dos.writeUTF(recipientName);
			dos.writeUTF(username);
			dos.writeLong(userID);
			dos.writeByte(gameType.toByte());
			
			sendData(INVITE_URI, baos.toByteArray());
			
		} catch (IOException e) {
			
			throw new AssertionError();
			
		}
		
	}
	
	@Override
	public void acceptInvite(long recipientID, long gameID, GameType gameType) {
		
		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos)
		) {
			
			dos.writeLong(recipientID);
			dos.writeUTF(username);
			dos.writeLong(userID);
			dos.writeLong(gameID);
			dos.writeByte(gameType.toByte());
			
			sendData(ACCEPT_INVITE_URI, baos.toByteArray());
			
		} catch (IOException e) {
			
			throw new AssertionError();
			
		}
		
	}
	
	private interface DataInputStreamConsumer {
		
		void accept(DataInputStream dis) throws IOException;
		
	}
	
	@Override
	public void asapMessagesReceived(ASAPMessages asapMessages, String s, List<ASAPHop> list) throws IOException {
		
		if (!asapMessages.getFormat().equals(ASAP_FORMAT))
			return;
		
		DataInputStreamConsumer consumer = switch (asapMessages.getURI().toString()) {
			case MOVE_URI -> this::onMoveMessages;
			case GLOBAL_INVITE_URI -> this::onGlobalInvite;
			case INVITE_URI -> this::onInvite;
			case ACCEPT_INVITE_URI -> this::onInviteAccepted;
			default -> null;
		};
		
		if (consumer == null) {
			
			System.err.printf("Unknown URI: %s%n", asapMessages.getURI());
			return;
			
		}
		
		Iterator<byte[]> iter = asapMessages.getMessages();
		
		while (iter.hasNext()) {
			
			byte[] bytes = iter.next();
			
			try (
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				DataInputStream dis = new DataInputStream(bais)
			) {
				
				consumer.accept(dis);
				
			} catch (IOException e) {
				
				//malformed message, can be ignored
				
			}
			
		}
		
	}
	
	private void onMoveMessages(DataInputStream dis) throws IOException {
		
		long recipientID = dis.readLong();
		
		if (recipientID != userID)
			return;
		
		long gameID = dis.readLong();
		
		byte[] message = new byte[dis.available()];
		dis.readFully(message);
		
		moveListener.onMove(gameID, message);
		
	}
	
	private void onGlobalInvite(DataInputStream dis) throws IOException {
		
		String senderName = dis.readUTF();
		long senderID = dis.readLong();
		GameType gameType = GameType.fromByte(dis.readByte());
		
		inviteListener.receiveInvite(senderName, senderID, gameType);
		
	}
	
	private void onInvite(DataInputStream dis) throws IOException {
		
		String recipientName = dis.readUTF();
		
		if (!recipientName.equalsIgnoreCase(username))
			return;
		
		String senderName = dis.readUTF();
		long senderID = dis.readLong();
		GameType gameType = GameType.fromByte(dis.readByte());
		
		inviteListener.receiveInvite(senderName, senderID, gameType);
		
	}
	
	private void onInviteAccepted(DataInputStream dis) throws IOException {
		
		long recipientID = dis.readLong();
		
		if (recipientID != userID)
			return;
		
		String senderName = dis.readUTF();
		long senderID = dis.readLong();
		long gameID = dis.readLong();
		GameType gameType = GameType.fromByte(dis.readByte());
		
		inviteListener.inviteAccepted(senderName, senderID, gameID, gameType);
		
	}
	
}