package radhoc.communication.impl;

import net.sharksystem.asap.*;
import radhoc.communication.Communication;
import radhoc.communication.InviteListener;
import radhoc.communication.MoveListener;
import radhoc.gamestates.GameType;

import java.io.*;
import java.util.ArrayList;
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
	
	private boolean listenersAvailable = false;
	private final List<Message> unreadMessages = new ArrayList<>();
	
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
	
	private synchronized void checkListeners() {
		
		if (moveListener != null && inviteListener != null)
			synchronized (unreadMessages) {
				
				listenersAvailable = true;
				
				for (Message message : unreadMessages)
					handleMessage(message);
				
			}
		
	}
	
	@Override
	public void setMoveListener(MoveListener listener) {
		
		moveListener = listener;
		
		checkListeners();
		
	}
	
	@Override
	public void setInviteListener(InviteListener listener) {
		
		inviteListener = listener;
		
		checkListeners();
		
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
	
	private static class Message {
		
		public final DataInputStreamConsumer consumer;
		public final byte[] bytes;
		
		public Message(DataInputStreamConsumer consumer, byte[] bytes) {
			this.consumer = consumer;
			this.bytes = bytes;
		}
		
	}
	
	@Override
	public void asapMessagesReceived(ASAPMessages messages, String s, List<ASAPHop> list) throws IOException {
		
		if (!messages.getFormat().equals(ASAP_FORMAT))
			return;
		
		DataInputStreamConsumer consumer = switch (messages.getURI().toString()) {
			case MOVE_URI -> this::onMove;
			case GLOBAL_INVITE_URI -> this::onGlobalInvite;
			case INVITE_URI -> this::onInvite;
			case ACCEPT_INVITE_URI -> this::onInviteAccepted;
			default -> null;
		};
		
		if (consumer == null) {
			
			System.err.printf("Unknown URI: %s%n", messages.getURI().toString());
			return;
			
		}
		
		if (!listenersAvailable)
			synchronized (unreadMessages) {
				
				if (!listenersAvailable) {
					
					for (var iter = messages.getMessages(); iter.hasNext();)
						unreadMessages.add(new Message(consumer, iter.next()));
					
					return;
					
				}
				
			}
		
		for (var iter = messages.getMessages(); iter.hasNext();)
			handleMessage(new Message(consumer, iter.next()));
		
	}
	
	private void handleMessage(Message message) {
		
		try (
			ByteArrayInputStream bais = new ByteArrayInputStream(message.bytes);
			DataInputStream dis = new DataInputStream(bais)
		) {
			
			message.consumer.accept(dis);
			
		} catch (IOException e) {
			
			System.err.printf("Received malformed message: %s%n", e.getMessage());
			
		}
		
	}
	
	private void onMove(DataInputStream dis) throws IOException {
		
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