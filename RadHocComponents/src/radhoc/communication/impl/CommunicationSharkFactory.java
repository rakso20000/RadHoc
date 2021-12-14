package radhoc.communication.impl;

import net.sharksystem.SharkComponent;
import net.sharksystem.SharkComponentFactory;

public class CommunicationSharkFactory implements SharkComponentFactory {
	
	private final long userID;
	private final String username;
	
	public CommunicationSharkFactory(long userID, String username) {
		
		this.userID = userID;
		this.username = username;
		
	}
	
	@Override
	public SharkComponent getComponent() {
		
		return new CommunicationImpl(userID, username);
		
	}
	
}