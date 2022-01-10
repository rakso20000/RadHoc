package radhoc.invitations.impl;

import radhoc.communication.Communication;
import radhoc.communication.InviteListener;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameType;
import radhoc.gamestates.UpdateListener;
import radhoc.gamestates.impl.GameStateImpl;
import radhoc.invitations.Invitation;
import radhoc.invitations.InvitationManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InvitationManagerImpl implements InvitationManager, InviteListener {
	
	private final GameStateManager gameStateManager;
	private final Communication communication;
	
	private final File invitationsFile;
	private final Random random = new Random();
	
	private final List<Invitation> invitations = new ArrayList<>();
	
	private UpdateListener updateListener;
	
	public InvitationManagerImpl(GameStateManager gameStateManager, Communication communication, File invitationsFile) {
		
		this.gameStateManager = gameStateManager;
		this.communication = communication;
		this.invitationsFile = invitationsFile;
		
		if (invitationsFile.exists()) {
			
			try (
				FileInputStream fis = new FileInputStream(invitationsFile);
				DataInputStream dis = new DataInputStream(fis)
			) {
				
				while (dis.available() > 0) {
					
					String opponentName = dis.readUTF();
					long opponentID = dis.readLong();
					GameType gameType = GameType.fromByte(dis.readByte());
					
					Invitation invitation = new InvitationImpl(this, opponentName, opponentID, gameType);
					invitations.add(invitation);
					
				}
				
			} catch (IOException e) {
				
				System.err.println("Failed to load Invitations from file " + invitationsFile.getName());
				e.printStackTrace();
				
				//noinspection ResultOfMethodCallIgnored
				invitationsFile.delete();
				
			}
			
		}
		
		communication.setInviteListener(this);
		
	}
	
	@Override
	public List<Invitation> getInvitations() {
		
		return new ArrayList<>(invitations);
		
	}
	
	@Override
	public void sendInvite(GameType gameType) {
		
		communication.sendInvite(gameType);
		
	}
	
	@Override
	public void sendInvite(String name, GameType gameType) {
		
		communication.sendInvite(name, gameType);
		
	}
	
	@Override
	public void receiveInvite(String senderName, long senderID, GameType gameType) {
		
		invitations.add(new InvitationImpl(this, senderName, senderID, gameType));
		
		update();
		
	}
	
	@Override
	public void inviteAccepted(String senderName, long senderID, long gameID, GameType gameType, boolean playerStarts) {
		
		gameStateManager.createGameState(gameType, senderName, senderID, gameID, playerStarts);
		
	}
	
	public void acceptInvitation(Invitation invitation) {
		
		long gameID = random.nextLong();
		boolean playerStarts = random.nextBoolean();
		
		communication.acceptInvite(invitation.getOpponentID(), gameID, invitation.getGameType(), !playerStarts);
		
		gameStateManager.createGameState(invitation.getGameType(), invitation.getOpponentName(), invitation.getOpponentID(), gameID, playerStarts);
		
		invitations.remove(invitation);
		
		update();
		
	}
	
	public void removeInvitation(Invitation invitation) {
		
		invitations.remove(invitation);
		
		update();
		
	}
	
	@Override
	public void setUpdateListener(UpdateListener listener) {
		
		updateListener = listener;
		
	}
	
	private void update() {
		
		if (updateListener != null)
			updateListener.onUpdate();
		
	}
	
	@Override
	public void save() {
		
		try {
			
			if (!invitationsFile.exists())
				//noinspection ResultOfMethodCallIgnored
				invitationsFile.createNewFile();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		try (
			FileOutputStream fos = new FileOutputStream(invitationsFile);
			DataOutputStream dos = new DataOutputStream(fos)
		) {
			
			for (Invitation invitation : invitations) {
				
				dos.writeUTF(invitation.getOpponentName());
				dos.writeLong(invitation.getOpponentID());
				dos.writeByte(invitation.getGameType().toByte());
				
			}
			
		} catch (IOException e) {
			
			System.err.println("Failed to save Invitations to file " + invitationsFile.getName());
			e.printStackTrace();
			
		}
		
	}
	
}