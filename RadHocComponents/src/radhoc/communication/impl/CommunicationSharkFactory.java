package radhoc.communication.impl;

import net.sharksystem.SharkComponent;
import net.sharksystem.SharkComponentFactory;
import radhoc.communication.Communication;

import java.util.concurrent.CompletableFuture;

public class CommunicationSharkFactory implements SharkComponentFactory {
	
	private final long userID;
	private final String username;
	private final CompletableFuture<Communication> communicationFuture;
	
	public CommunicationSharkFactory(long userID, String username, CompletableFuture<Communication> communicationFuture) {
		
		this.userID = userID;
		this.username = username;
		this.communicationFuture = communicationFuture;
		
	}
	
	@Override
	public SharkComponent getComponent() {
		
		return new CommunicationImpl(userID, username, communicationFuture);
		
	}
	
}